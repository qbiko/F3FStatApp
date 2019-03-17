package pl.f3f_klif.f3fstatapp.groups.strategy.menu;

public class StrategyScope {
    public long groupId;
    public long roundId;

    public StrategyScope(long groupId, long roundId){
       this.groupId = groupId;
       this.roundId = roundId;
    }

    public StrategyScope(long roundId){
        this.roundId = roundId;
    }
}
