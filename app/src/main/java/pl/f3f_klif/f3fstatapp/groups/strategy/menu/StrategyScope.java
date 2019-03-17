package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

import android.content.Context;

public class StrategyScope {
    public long groupId;
    public long roundId;
    public Context context;

    public StrategyScope(long groupId, long roundId, Context context){
       this.groupId = groupId;
       this.roundId = roundId;
       this.context = context;
    }

    public StrategyScope(long roundId, Context context){
        this.roundId = roundId;
        this.context = context;
    }
}
