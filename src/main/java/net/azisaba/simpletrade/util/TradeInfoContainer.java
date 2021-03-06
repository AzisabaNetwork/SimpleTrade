package net.azisaba.simpletrade.util;

import lombok.RequiredArgsConstructor;
import net.azisaba.simpletrade.SimpleTrade;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * トレード情報を格納するクラス
 *
 * @author siloneco
 */
@RequiredArgsConstructor
public class TradeInfoContainer {

    private final SimpleTrade plugin;

    // 現在取引中の情報を格納するHashMap
    private final HashMap<Player, TradeInfo> tradingMap = new HashMap<>();

    public void create(Player player1, Player player2) {
        TradeInfo info = new TradeInfo(player1, player2);
        info.startTrade();

        tradingMap.put(player1, info);
        tradingMap.put(player2, info);
    }

    /**
     * @param p 対象プレイヤー
     * @return 対象プレイヤーが行っている取引の {@link TradeInfo} 、存在しない場合は null
     */
    public TradeInfo getTradeInfo(Player p) {
        TradeInfo info = tradingMap.getOrDefault(p, null);
        if (info != null && info.isFinished()) {
            tradingMap.remove(p);
            return null;
        }
        return info;
    }

    public void onDisable() {
        // TradeInfoがまだある場合は戻す
        List<TradeInfo> infoList = new ArrayList<>();
        for (TradeInfo info : tradingMap.values()) {
            if (!infoList.contains(info)) {
                infoList.add(info);
            }
        }

        for (TradeInfo tradeInfo : infoList) {
            if (!tradeInfo.isFinished()) {
                tradeInfo.close();
            }
        }
    }

    public int getTrades() {
        for (Player player : new ArrayList<>(tradingMap.keySet())) {
            if (tradingMap.get(player).isFinished()) {
                tradingMap.remove(player);
            }
        }
        return tradingMap.size();
    }
}
