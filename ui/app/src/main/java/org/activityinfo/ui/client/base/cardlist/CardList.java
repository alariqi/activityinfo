package org.activityinfo.ui.client.base.cardlist;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;

public class CardList {


    public static VTree render(List<? extends Card> cards) {
        return H.div("cardlist",
                cards.stream().map(CardList::renderCard));
    }

    private static VTree renderCard(Card card) {
        return H.div("card",
                H.div("surtitle", new VText(card.getSurtitle())),
                H.div("card__label", new VText(card.getLabel())));
    }
}
