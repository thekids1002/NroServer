package nro.card;

import nro.item.ItemOption;

import java.util.ArrayList;
import nro.main.Util;

public class RadaCardManager {
    private final ArrayList<RadaCard> cards = new ArrayList<>();
    private static RadaCardManager instance;

    public static RadaCardManager gI() {
        if (instance == null) {
            instance = new RadaCardManager();
        }
        return instance;
    }

    public void init() {
        Util.log("Load list rada card");
        RadaCard _card = null;
        ItemOption _option = null;
        //THE KHUNG LONG
        _card = new RadaCard();
        _card.id = (short)828;
        _card.idIcon = (short)7467;
        _card.rank = (byte)0;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)1;
        _card.name = "Thẻ Khủng Long";
        _card.info = "Hai chi trước của Khủng long rất ngắn nên chúng không thể cầm thức ăn được";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(6, 10, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(6, 15, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(6, 25, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //THE LON LOI
        _card = new RadaCard();
        _card.id = (short)829;
        _card.idIcon = (short)7468;
        _card.rank = (byte)0;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)2;
        _card.name = "Thẻ Lợn lòi";
        _card.info = "Lợn lòi có sở thích mài răng nanh dưới đất, vô tình tạo ra những rãnh đất để người Namec trồng trọt";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(7, 10, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(7, 15, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(7, 25, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //THE QUY DAT
        _card = new RadaCard();
        _card.id = (short)830;
        _card.idIcon = (short)7469;
        _card.rank = (byte)0;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)3;
        _card.name = "Thẻ Quỷ đất";
        _card.info = "Bản tính của Qủy đất khá nhút nhát, chúng thường núp vào cây khi gặp người lạ";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(0, 1, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 3, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 5, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //THE KHUNG LONG ME
        _card = new RadaCard();
        _card.id = (short)831;
        _card.idIcon = (short)7470;
        _card.rank = (byte)1;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)4;
        _card.name = "Thẻ Khủng long mẹ";
        _card.info = "Hai chi trước của Khủng long rất ngắn nên chúng không thể cầm thức ăn được";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(6, 15, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(6, 25, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(6, 30, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Lợn lòi mẹ
        _card = new RadaCard();
        _card.id = (short)832;
        _card.idIcon = (short)7471;
        _card.rank = (byte)1;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)5;
        _card.name = "Thẻ Lợn lòi mẹ";
        _card.info = "Lợn lòi có sở thích mài răng nanh dưới đất, vô tình tạo ra những rãnh đất để người Namec trồng trọt";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(7, 15, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(7, 25, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(7, 30, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Quỷ đất mẹ
        _card = new RadaCard();
        _card.id = (short)833;
        _card.idIcon = (short)7472;
        _card.rank = (byte)1;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)6;
        _card.name = "Thẻ Quỷ đất mẹ";
        _card.info = "Bản tính của Qủy đất khá nhút nhát, chúng thường núp vào cây khi gặp người lạ";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(0, 3, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 5, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 7, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Thằn lằn bay
        _card = new RadaCard();
        _card.id = (short)834;
        _card.idIcon = (short)7473;
        _card.rank = (byte)2;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)7;
        _card.name = "Thẻ Thằn lằn bay";
        _card.info = "Thằn lằn bay dùng cái mỏ dài và cứng để tấn công kẻ thù, đôi khi chúng vẫn dùng để gõ hạt óc chó ăn";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(27, 1, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(27, 3, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(27, 5, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Phi long,
        _card = new RadaCard();
        _card.id = (short)835;
        _card.idIcon = (short)7474;
        _card.rank = (byte)2;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)8;
        _card.name = "Thẻ Phi long";
        _card.info = "Dùng tốc độ cực nhanh để tiếp cận và hạ gục mục tiêu, Phi long cũng có sở thích hơi lạ là khi bay luôn mở to miệng để đón gió";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(28, 1, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(28, 3, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(28, 5, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Quỷ bay
        _card = new RadaCard();
        _card.id = (short)836;
        _card.idIcon = (short)7475;
        _card.rank = (byte)2;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)9;
        _card.name = "Thẻ Quỷ bay";
        _card.info = "Sở trường tấn công chớp choáng kẻ thù, nhưng đâu ai ngờ Quỷ bay lại có bệnh sợ độ cao";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(47, 5, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(47, 10, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(47, 15, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Lính độc nhãn
        _card = new RadaCard();
        _card.id = (short)837;
        _card.idIcon = (short)7476;
        _card.rank = (byte)3;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)34;
        _card.name = "Thẻ Lính độc nhãn";
        _card.info = "Người ta nuôi quân lính dùng trong 1 giờ, Lính độc nhãn thì không dùng được 5 phút";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(7, 30, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 5, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(6, 50, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Lính độc nhãn
        _card = new RadaCard();
        _card.id = (short)838;
        _card.idIcon = (short)7477;
        _card.rank = (byte)3;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)35;
        _card.name = "Thẻ Lính độc nhãn";
        _card.info = "Người ta nuôi quân lính dùng trong 1 giờ, Lính độc nhãn thì không dùng được 5 phút";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(6, 50, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(7, 50, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 5, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Sói xám
        _card = new RadaCard();
        _card.id = (short)839;
        _card.idIcon = (short)7478;
        _card.rank = (byte)3;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)0;
        _card.temp_mob = (short)36;
        _card.name = "Thẻ Sói xám";
        _card.info = "Được lính độc nhãn thuần hóa và cho giữ nhà như cún con, sở thích của chúng là được chơi trò nhặt bóng";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(0, 5, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(7, 50, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(6, 30, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Trung úy Trắng
        _card = new RadaCard();
        _card.id = (short)840;
        _card.idIcon = (short)7480;
        _card.rank = (byte)4;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)1;
        _card.head = (short)141;
        _card.body = (short)142;
        _card.leg = (short)143;
        _card.bag = (short)(-1);
        _card.temp_mob = (short)(-1);
        _card.name = "Thẻ Trung úy Trắng";
        _card.info = "Thân hình hơi béo so với tiêu chuẩn chung, luôn đeo chiếc khăn quàng đỏ, thường hay mơ mộng làm thơ";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(47, 15, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 15, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(47, 50, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Trung úy Trắng
        _card = new RadaCard();
        _card.id = (short)841;
        _card.idIcon = (short)7481;
        _card.rank = (byte)4;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)1;
        _card.head = (short)123;
        _card.body = (short)124;
        _card.leg = (short)125;
        _card.bag = (short)(-1);
        _card.temp_mob = (short)(-1);
        _card.name = "Thẻ Ninja Áo Tím";
        _card.info = "Ninja với nhiều tài năng để trở thành sát thủ, nhưng kỹ thuật ẩn thân lại không có, nên không thể trở thành sát thủ";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(7, 100, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(114, 1, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(114, 1, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Trung úy Xanh Lơ
        _card = new RadaCard();
        _card.id = (short)842;
        _card.idIcon = (short)7479;
        _card.rank = (byte)4;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)1;
        _card.head = (short)135;
        _card.body = (short)136;
        _card.leg = (short)137;
        _card.bag = (short)(-1);
        _card.temp_mob = (short)(-1);
        _card.name = "Thẻ Trung úy Xanh Lơ";
        _card.info = "Có siêu năng lực thôi miên nhưng cực kỳ sợ chuột";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(6, 100, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 20, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(0, 20, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Độc Nhãn
        _card = new RadaCard();
        _card.id = (short)859;
        _card.idIcon = (short)1568;
        _card.rank = (byte)5;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)1;
        _card.head = (short)144;
        _card.body = (short)145;
        _card.leg = (short)146;
        _card.bag = (short)(-1);
        _card.temp_mob = (short)(-1);
        _card.name = "Thẻ Độc Nhãn";
        _card.info = "Đầu não của Red Ribbon. Bị chột một mắt. Lúc nhỏ bị mọi người chê là 'Thằng lùn'";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(175, 5, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(175, 8, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(175, 11, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);

        //Thẻ Đội trưởng Vàng
        _card = new RadaCard();
        _card.id = (short)956;
        _card.idIcon = (short)8935;
        _card.rank = (byte)4;
        _card.amount = (byte)0;
        _card.max_amount = (byte)120;
        _card.is_cardmob = (byte)1;
        _card.head = (short)994;
        _card.body = (short)995;
        _card.leg = (short)996;
        _card.bag = (short)(-1);
        _card.temp_mob = (short)(-1);
        _card.name = "Thẻ Đội trưởng Vàng";
        _card.info = "Đội trưởng Vàng là một con hổ hình người chắc nịch có cơ thể được bao phủ bởi bộ lông màu vàng, Goku đã đấm hắn ra khỏi máy bay của mình khi đang ở giữa không trung.";
        _card.level = (byte)0;
        _card.set_use = (byte)0;
        _option = new ItemOption();
        _option.ItemOptionCard(50, 1, (byte)0);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(50, 2, (byte)1);
        _card.itemOptions.add(_option);
        _option = new ItemOption();
        _option.ItemOptionCard(50, 3, (byte)2);
        _card.itemOptions.add(_option);
        cards.add(_card);
        Util.log("Finish load list rada card: " + cards.size());
    }

    public ArrayList<RadaCard> getCards() {
        return cards;
    }

    public RadaCard getCardById(short id) {
        for (RadaCard card : cards) {
            if (card.id == id) {
                return card;
            }
        }
        return null;
    }
}
