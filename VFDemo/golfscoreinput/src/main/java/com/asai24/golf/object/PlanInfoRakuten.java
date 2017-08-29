package com.asai24.golf.object;

/**
 * Created by huynq on 8/3/16.
 */
public class PlanInfoRakuten extends ObjectParent {
    private PlanItemRakuten plan;

    public PlanItemRakuten getPlan() {
        return this.plan;
    }

    public void setPlan(PlanItemRakuten plan) {
        this.plan = plan;
    }
}
