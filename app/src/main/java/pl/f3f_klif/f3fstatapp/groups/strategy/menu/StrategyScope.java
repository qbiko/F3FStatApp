package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.content.Context;

public class StrategyScope {
    public long groupId;
    public long roundId;
    public Context context;
    public int roundIndex;

    public StrategyScope(long groupId, long roundId, Context context, int roundIndex){
       this.groupId = groupId;
       this.roundId = roundId;
       this.context = context;
       this.roundIndex = roundIndex;
    }

    public StrategyScope(long roundId, Context context, int roundIndex){
        this.roundId = roundId;
        this.context = context;
        this.roundIndex = roundIndex;
    }
}
