/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2022 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2022 Yue Li <yueli@nju.edu.cn>
 *
 * This file is part of Tai-e.
 *
 * Tai-e is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Tai-e is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Tai-e. If not, see <https://www.gnu.org/licenses/>.
 */

package pascal.taie.analysis.dataflow.analysis;

import pascal.taie.analysis.dataflow.fact.SetFact;
import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.ir.exp.LValue;
import pascal.taie.ir.exp.RValue;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.Stmt;


/**
 * Implementation of classic live variable analysis.
 */
public class LiveVariableAnalysis extends
        AbstractDataflowAnalysis<Stmt, SetFact<Var>> {

    public static final String ID = "livevar";

    public LiveVariableAnalysis(AnalysisConfig config) {
        super(config);
    }

    @Override
    public boolean isForward() {
        return false;
    }

    @Override
    public SetFact<Var> newBoundaryFact(CFG<Stmt> cfg) {
        // TODO - finish me
        // 返回边界节点的向量，backwards的边界节点是 exit节点
        return new SetFact<Var>();
    }

    @Override
    public SetFact<Var> newInitialFact() {  // 返回值就是 SetFact<Var>
        // TODO - finish me
        // 除了exit节点的其他节点初始化为空
        return new SetFact<Var>();
    }

    @Override
    public void meetInto(SetFact<Var> fact, SetFact<Var> target) { // 注意这里提示了，使用的是SetFact<Var>， 所以前面用的也都是Var
        // TODO - finish me
        // 并起来
        target.union(fact);
    }

    @Override
    public boolean transferNode(Stmt stmt, SetFact<Var> in, SetFact<Var> out) {
        // TODO - finish me
        // 复制outFact，求出新的inFact
        SetFact<Var> newInFact = new SetFact<>();
        newInFact.union(out);

        // 求出新的inFact
        // -def
        if (stmt.getDef().isPresent()) { // 先用isPresent再用get是optional的典型用法
            LValue def = stmt.getDef().get();
            if (def instanceof Var) {
                newInFact.remove((Var) def);
            }
        }
        // +use
        for (RValue use : stmt.getUses()) {
            if (use instanceof  Var) {
                newInFact.add((Var) use);
            }
        }
        // 判断inFact是否改变，并返回Boolean表示
        if (!newInFact.equals(in)) {
            in.set(newInFact);
            return true;
        }
        return false;
    }
}
