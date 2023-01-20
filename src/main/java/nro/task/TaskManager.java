package nro.task;

import java.util.ArrayList;
import nro.main.Util;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    public String[][] detailTASK  = {  
        {
            "Nhiệm vụ đầu tiên",
            "Mộc nhân được đặt nhiều tại Làng,\nngay trước nhà\nHãy đánh ngã 5 mộc nhân, sau đó quay\nvề nhà báo cáo với ông Gohan\nĐể đánh, hãy click đôi vào đối tượng\nThưởng 500 sức mạnh\nThưởng 500 tiềm năng",
            "Khủng long xuất hiện tại Đồi hoa cúc.\nHãy đánh bại khủng long, thu thập đủ 10\nđùi gà sau đó quay về nhà báo cáo với ông Gohan.\nĐể đánh, hãy click đôi vào đối tượng.\nThưởng 1 k sức mạnh\nThưởng 1 k tiềm năng",
            "Dùng tiềm năng con có để nâng cấp\nHP,KI hoặc Sức đánh\nKiểm tra khu vực sao băng rơi tại Vách núi Aru\nThưởng 2 k sức mạnh\nThưởng 2 k tiềm năng",
            "Khủng long mẹ sống tại Trái Đất\nLợn lòi mẹ sống tại Namếc\nQuỷ đất mẹ sống tại Xayda\nDùng tàu vũ trụ để di chuyển sang hành\nkhác\nThưởng 4 k sức mạnh\nThưởng 4 k tiềm năng",
            "5",
            "6",
            "Tìm đường đến Rừng nấm,\nHạ 20 con Thằn lằn bay\nThưởng 8 k sức mạnh\nThưởng 8 k tiềm năng",
            "Ngọc rồng 7 sao đang bị bọn Phi\nlong mẹ cướp đi tại Vực Maima, Namếc.\nĐánh bại chúng để tìm lại.\nThưởng 15 k sức mạnh\nThưởng 15 k tiềm năng",
            "Tìm Quy Lão tại đảo Kame bái sư học\nvõ\nThưởng 5 k sức mạnh\nThưởng 5 k tiềm năng",
            "10",
            "11",
            "Báo cáo với Quy Lão khi bang của\nbạn có từ 5 thành viên trở lên\nThưởng 20 k sức mạnh\nThưởng 20 k tiềm năng",
            "Cùng ít nhất 2 thành viên trong\nbang tiêu diệt\nHeo rừng tại Rừng dương xỉ(Trái Đất)\nHeo da xanh tại Núi hoa tím(Namếc)\nHeo Xayda tại Rừng đá(Xayda)\nThưởng 50 k sức mạnh\nThưởng 50 k tiềm năng",
            "Đánh bọn Ốc mượn hồn để lấy truyện\nDoremon tập 2\nThưởng 80 k sức mạnh\nThưởng 80 k tiềm năng",
            "Cùng ít nhất 2 thành viên trong\nbang tiêu diệt\nBulon tại Đảo Bulông(Trái Đất)\nUkulele tại Đông Nam Guru(Namếc)\nQuỷ mập tại Bờ Vực Đen(Xayda)\nThưởng 150 k sức mạnh\nThưởng 150 k tiềm năng",
            "Thách đấu và chiến thằng 10 người\nbất kì\nThưởng 150 k sức mạnh\nThưởng 150 k tiềm năng",
            "Đạt 1.500.000 sức mạnh để trở\nthành Siêu nhân\nTiêu diệt Akkuman tại Thành\nphố Vegeta, tiêu diệt Tamborine tại Đông\nKarin, tiêu diệt Drum tại Thung\nlũng Namếc\nThưởng 200 k sức mạnh\nThưởng 200 k tiềm năng",
            "Đạt 5 triệu sức mạnh\nTham gia và chiến thằng vòng 2 đại hội\nvõ thuật tại Vách núi Kakarot\nThưởng 500 k sức mạnh\nThưởng 500 k tiềm năng",
            "Đạt 15 triệu sức mạnh\nVào doanh trại Độc Nhãn tìm diệt\nTrung Úy Trắng\nThưởng 5 Tr sức mạnh\nThưởng 5 Tr tiềm năng",
            "Đạt 50 triệu sức mạnh\nTiêu diệt bọn tay sai của Fide tại Xayda\nThưởng 50 Tr sức mạnh\nThưởng 50 Tr tiềm năng",
            "Tiêu diệt bọn đệ tử Kuku, Mập Đầu Đinh,\nRambo của Fide đại ca tại Xayda\nCui có thể biết vị trí của chúng, nếu tìm\nkhông thấy hãy đến gặp Cui tại thành\nphố Vegeta\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Tiêu diệt Tiểu Đội Sát Thủ do Fide đại\nca gọi đến tại Xayda\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Fide đã xuất hiện tại núi khỉ vàng\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Đến trái đất, rừng bamboo, rừng dương\nxỉ, nam Kamê tìm người lạ\nĐến đảo rùa đưa thuốc cho Quy Lão\nTheo Ca Lích đến tương lai\nGiúp họ diệt bọn bọ hung con\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Hãy đến thành phố phía nam\nđảo balê hoặc cao nguyên\nCùng 2 đồng bang diệt 900 Xên con cấp 3\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Trở về quá khứ, đến sân sau siêu thị\nTiêu diệt bọn Rôbốt sát thủ\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Đến thành phố, ngọn núi, thung lũng phía Bắc\nTiêu diệt bọn Rôbốt sát thủ\nCùng 2 đồng bang diệt 800 Xên con cấp 5\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Đến thị trấn Ginder\nTiêu diệt Xên Bọ Hung cấp 1\nTiêu diệt Xên Bọ Hung cấp 2\nTiêu diệt Xên Bọ Hung hoàn thiện\nCùng 2 đồng bang diệt 700 Xên con cấp 8\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Nâng sức đánh gốc lên 10K, đến gặp thần\nmèo\nThu thập Capsule kì bí\nĐến võ đài Xên Bọ Hung\nTiêu diệt 7 đứa con của Xên\nTiêu diệt Siêu Bọ Hung\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Cẩn thận !!!\nNhững vị khách không mời mà tới\nthường tỏ ra nguy hiểm",
            "Ai mới thật sự là siêu xayda huyền thoại\nmà Fide từng nhắc tới ?\nThưởng 10 Tr sức mạnh\nThưởng 10 Tr tiềm năng",
            "Bảo vệ hành tinh thực vật, hạ những kẻ\nxâm lược.\nThưởng 10 Tr sức mạnh\nThưởng 10 Tr tiềm năng",
            "Chưa có nhiệm vụ mới",
            "Chưa có nhiệm vụ mới",
            "Chưa có nhiệm vụ mới",
        },
        {
            "Nhiệm vụ đầu tiên",
            "Mộc nhân được đặt nhiều tại Làng,\nngay trước nhà\nHãy đánh ngã 5 mộc nhân, sau đó quay\nvề nhà báo cáo với ông Morri\nĐể đánh, hãy click đôi vào đối tượng\nThưởng 500 sức mạnh\nThưởng 500 tiềm năng",
            "Lợn lòi xuất hiện tại Đồi nấm tím.\nHãy đánh bại lợn lòi, thu thập đủ 10\nđùi gà sau đó quay về nhà báo cáo với ông Morri.\nĐể đánh, hãy click đôi vào đối tượng.\nThưởng 1 k sức mạnh\nThưởng 1 k tiềm năng",
            "Dùng tiềm năng con có để nâng cấp\nHP,KI hoặc Sức đánh\nKiểm tra khu vực sao băng rơi tại Vách núi Morri\nThưởng 2 k sức mạnh\nThưởng 2 k tiềm năng",
            "Khủng long mẹ sống tại Trái Đất\nLợn lòi mẹ sống tại Namếc\nQuỷ đất mẹ sống tại Xayda\nDùng tàu vũ trụ để di chuyển sang hành\nkhác\nThưởng 4 k sức mạnh\nThưởng 4 k tiềm năng",
            "5",
            "6",
            "Tìm đường đến Thung lũng Maima,\nHạ 20 con Phi long\nThưởng 8 k sức mạnh\nThưởng 8 k tiềm năng",
            "Ngọc rồng 7 sao đang bị bọn Quỷ\nbay mẹ cướp đi tại Rừng thông Xayda, Xayda.\nĐánh bại chúng để tìm lại.\nThưởng 15 k sức mạnh\nThưởng 15 k tiềm năng",
            "Tìm Trưởng Lão Guru tại đảo Guru bái sư\nhọc võ\nThưởng 5 k sức mạnh\nThưởng 5 k tiềm năng",
            "10",
            "11",
            "Báo cáo với Trưởng Lão Guru khi bang\ncủa bạn có từ 5 thành viên trở lên\nThưởng 20 k sức mạnh\nThưởng 20 k tiềm năng",
            "Cùng ít nhất 2 thành viên trong\nbang tiêu diệt\nHeo rừng tại Rừng dương xỉ(Trái Đất)\nHeo da xanh tại Núi hoa tím(Namếc)\nHeo Xayda tại Rừng đá(Xayda)\nThưởng 50 k sức mạnh\nThưởng 50 k tiềm năng",
            "Đánh bọn Ốc sên để lấy truyện\nDoremon tập 2\nThưởng 80 k sức mạnh\nThưởng 80 k tiềm năng",
            "Cùng ít nhất 2 thành viên trong\nbang tiêu diệt\nBulon tại Đảo Bulông(Trái Đất)\nUkulele tại Đông Nam Guru(Namếc)\nQuỷ mập tại Bờ Vực Đen(Xayda)\nThưởng 150 k sức mạnh\nThưởng 150 k tiềm năng",
            "Thách đấu và chiến thằng 10 người\nbất kì\nThưởng 150 k sức mạnh\nThưởng 150 k tiềm năng",
            "Đạt 1.500.000 sức mạnh để trở\nthành Siêu Nammếc\nTiêu diệt Akkuman tại Thành\nphố Vegeta, tiêu diệt Tamborine tại Đông\nKarin, tiêu diệt Drum tại Thung\nlũng Namếc\nThưởng 200 k sức mạnh\nThưởng 200 k tiềm năng",
            "Đạt 5 triệu sức mạnh\nTham gia và chiến thằng vòng 2 đại hội\nvõ thuật tại Vách núi Kakarot\nThưởng 500 k sức mạnh\nThưởng 500 k tiềm năng",
            "Đạt 15 triệu sức mạnh\nVào doanh trại Độc Nhãn tìm diệt\nTrung Úy Trắng\nThưởng 5 Tr sức mạnh\nThưởng 5 Tr tiềm năng",
            "Đạt 50 triệu sức mạnh\nTiêu diệt bọn tay sai của Fide tại Xayda\nThưởng 50 Tr sức mạnh\nThưởng 50 Tr tiềm năng",
            "Tiêu diệt bọn đệ tử Kuku, Mập Đầu Đinh,\nRambo của Fide đại ca tại Xayda\nCui có thể biết vị trí của chúng, nếu tìm\nkhông thấy hãy đến gặp Cui tại thành\nphố Vegeta\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Tiêu diệt Tiểu Đội Sát Thủ do Fide đại\nca gọi đến tại Xayda\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Fide đã xuất hiện tại núi khỉ vàng\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Đến trái đất, rừng bamboo, rừng dương\nxỉ, nam Kamê tìm người lạ\nĐến đảo rùa đưa thuốc cho Quy Lão\nTheo Ca Lích đến tương lai\nGiúp họ diệt bọn bọ hung con\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Hãy đến thành phố phía nam\nđảo balê hoặc cao nguyên\nCùng 2 đồng bang diệt 900 Xên con cấp 3\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Trở về quá khứ, đến sân sau siêu thị\nTiêu diệt bọn Rôbốt sát thủ\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Đến thành phố, ngọn núi, thung lũng phía Bắc\nTiêu diệt bọn Rôbốt sát thủ\nCùng 2 đồng bang diệt 800 Xên con cấp 5\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Đến thị trấn Ginder\nTiêu diệt Xên Bọ Hung cấp 1\nTiêu diệt Xên Bọ Hung cấp 2\nTiêu diệt Xên Bọ Hung hoàn thiện\nCùng 2 đồng bang diệt 700 Xên con cấp 8\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Nâng sức đánh gốc lên 10K, đến gặp thần\nmèo\nThu thập Capsule kì bí\nĐến võ đài Xên Bọ Hung\nTiêu diệt 7 đứa con của Xên\nTiêu diệt Siêu Bọ Hung\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Cẩn thận !!!\nNhững vị khách không mời mà tới\nthường tỏ ra nguy hiểm",
            "Ai mới thật sự là siêu xayda huyền thoại\nmà Fide từng nhắc tới ?\nThưởng 10 Tr sức mạnh\nThưởng 10 Tr tiềm năng",
            "Bảo vệ hành tinh thực vật, hạ những kẻ\nxâm lược.\nThưởng 10 Tr sức mạnh\nThưởng 10 Tr tiềm năng",
            "Chưa có nhiệm vụ mới",
            "Chưa có nhiệm vụ mới",
            "Chưa có nhiệm vụ mới",
        },
        {
            "Nhiệm vụ đầu tiên",
            "Mộc nhân được đặt nhiều tại Làng,\nngay trước nhà\nHãy đánh ngã 5 mộc nhân, sau đó quay\nvề nhà báo cáo với ông Paragus\nĐể đánh, hãy click đôi vào đối tượng\nThưởng 500 sức mạnh\nThưởng 500 tiềm năng",
            "Quỷ đất xuất hiện tại Đồi hoang.\nHãy đánh bại quỷ đất, thu thập đủ 10\nđùi gà sau đó quay về nhà báo cáo với ông Paragus.\nĐể đánh, hãy click đôi vào đối tượng.\nThưởng 1 k sức mạnh\nThưởng 1 k tiềm năng",
            "Dùng tiềm năng con có để nâng cấp\nHP,KI hoặc Sức đánh\nKiểm tra khu vực sao băng rơi tại Vách núi Kakarot\nThưởng 2 k sức mạnh\nThưởng 2 k tiềm năng",
            "Khủng long mẹ sống tại Trái Đất\nLợn lòi mẹ sống tại Namếc\nQuỷ đất mẹ sống tại Xayda\nDùng tàu vũ trụ để di chuyển sang hành\nkhác\nThưởng 4 k sức mạnh\nThưởng 4 k tiềm năng",
            "5",
            "6",
            "Tìm đường đến Rừng nguyên sinh,\nHạ 20 con Quỷ bay\nThưởng 8 k sức mạnh\nThưởng 8 k tiềm năng",
            "Ngọc rồng 7 sao đang bị bọn Thằn\nlằn mẹ cướp đi tại Rừng Xương, Trái Đất.\nĐánh bại chúng để tìm lại.\nThưởng 15 k sức mạnh\nThưởng 15 k tiềm năng",
            "Tìm Vua Vegeta tại vách núi đen bái sư\nhọc võ\nThưởng 5 k sức mạnh\nThưởng 5 k tiềm năng",
            "10",
            "11",
            "Báo cáo với Vua Vegeta khi bang\ncủa bạn có từ 5 thành viên trở lên\nThưởng 20 k sức mạnh\nThưởng 20 k tiềm năng",
            "Cùng ít nhất 2 thành viên trong\nbang tiêu diệt\nHeo rừng tại Rừng dương xỉ(Trái Đất)\nHeo da xanh tại Núi hoa tím(Namếc)\nHeo Xayda tại Rừng đá(Xayda)\nThưởng 50 k sức mạnh\nThưởng 50 k tiềm năng",
            "Đánh bọn Heo xayda mẹ để lấy truyện\nDoremon tập 2\nThưởng 80 k sức mạnh\nThưởng 80 k tiềm năng",
            "Cùng ít nhất 2 thành viên trong\nbang tiêu diệt\nBulon tại Đảo Bulông(Trái Đất)\nUkulele tại Đông Nam Guru(Namếc)\nQuỷ mập tại Bờ Vực Đen(Xayda)\nThưởng 150 k sức mạnh\nThưởng 150 k tiềm năng",
            "Thách đấu và chiến thằng 10 người\nbất kì\nThưởng 150 k sức mạnh\nThưởng 150 k tiềm năng",
            "Đạt 1.500.000 sức mạnh để trở\nthành Siêu Xayda\nTiêu diệt Akkuman tại Thành\nphố Vegeta, tiêu diệt Tamborine tại Đông\nKarin, tiêu diệt Drum tại Thung\nlũng Namếc\nThưởng 200 k sức mạnh\nThưởng 200 k tiềm năng",
            "Đạt 5 triệu sức mạnh\nTham gia và chiến thằng vòng 2 đại hội\nvõ thuật tại Vách núi Kakarot\nThưởng 500 k sức mạnh\nThưởng 500 k tiềm năng",
            "Đạt 15 triệu sức mạnh\nVào doanh trại Độc Nhãn tìm diệt\nTrung Úy Trắng\nThưởng 5 Tr sức mạnh\nThưởng 5 Tr tiềm năng",
            "Đạt 50 triệu sức mạnh\nTiêu diệt bọn tay sai của Fide tại Xayda\nThưởng 50 Tr sức mạnh\nThưởng 50 Tr tiềm năng",
            "Tiêu diệt bọn đệ tử Kuku, Mập Đầu Đinh,\nRambo của Fide đại ca tại Xayda\nCui có thể biết vị trí của chúng, nếu tìm\nkhông thấy hãy đến gặp Cui tại thành\nphố Vegeta\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Tiêu diệt Tiểu Đội Sát Thủ do Fide đại\nca gọi đến tại Xayda\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Fide đã xuất hiện tại núi khỉ vàng\nThưởng 20 Tr sức mạnh\nThưởng 20 Tr tiềm năng",
            "Đến trái đất, rừng bamboo, rừng dương\nxỉ, nam Kamê tìm người lạ\nĐến đảo rùa đưa thuốc cho Quy Lão\nTheo Ca Lích đến tương lai\nGiúp họ diệt bọn bọ hung con\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Hãy đến thành phố phía nam\nđảo balê hoặc cao nguyên\nCùng 2 đồng bang diệt 900 Xên con cấp 3\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Trở về quá khứ, đến sân sau siêu thị\nTiêu diệt bọn Rôbốt sát thủ\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Đến thành phố, ngọn núi, thung lũng phía Bắc\nTiêu diệt bọn Rôbốt sát thủ\nCùng 2 đồng bang diệt 800 Xên con cấp 5\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Đến thị trấn Ginder\nTiêu diệt Xên Bọ Hung cấp 1\nTiêu diệt Xên Bọ Hung cấp 2\nTiêu diệt Xên Bọ Hung hoàn thiện\nCùng 2 đồng bang diệt 700 Xên con cấp 8\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Nâng sức đánh gốc lên 10K, đến gặp thần\nmèo\nThu thập Capsule kì bí\nĐến võ đài Xên Bọ Hung\nTiêu diệt 7 đứa con của Xên\nTiêu diệt Siêu Bọ Hung\nBáo với Bunma tương lai\nThưởng 1 Tr sức mạnh\nThưởng 1 Tr tiềm năng",
            "Cẩn thận !!!\nNhững vị khách không mời mà tới\nthường tỏ ra nguy hiểm",
            "Ai mới thật sự là siêu xayda huyền thoại\nmà Fide từng nhắc tới ?\nThưởng 10 Tr sức mạnh\nThưởng 10 Tr tiềm năng",
            "Bảo vệ hành tinh thực vật, hạ những kẻ\nxâm lược.\nThưởng 10 Tr sức mạnh\nThưởng 10 Tr tiềm năng",
            "Chưa có nhiệm vụ mới",
            "Chưa có nhiệm vụ mới",
            "Chưa có nhiệm vụ mới",
        }
    };
    public String[][] subnameTASK0 = {
        {"Nhiệm vụ đầu tiên"},
        {"Đánh ngã 5 mộc nhân","Báo cáo với ông Gohan"},
        {"Thu thập 10 đùi gà","Báo cáo với ông Gohan"},
        {"Dùng tiềm năng","Tìm kiếm sao băng","Báo cáo với ông Gohan"},
        {"Đánh 3 con khủng long mẹ","Đánh 3 con lợn lòi mẹ","Đánh 3 con quỷ đất mẹ","Báo cáo với ông Gohan"},
        {"5"},
        {"6"},
        {"Đạt 16.000 sức mạnh","Đánh bại 20 con Thằn lằn bay","Nói chuyện với Bunma","Báo cáo với ông Gohan"},
        {"Đạt 40.000 sức mạnh","Tìm viên ngọc rồng 7 sao","Đem ngọc về cho ông Gohan","Tìm tháp Karin"},
        {"Tìm Quỹ Lão tại Đảo Kame","Báo cáo với ông Gohan"},
        {"10"},
        {"11"},
        {"Vào 1 bang hội","Báo cáo với Quy Lão"},
        {"Tiêu diệt Heo rừng","Tiêu diệt Heo da xanh","Tiêu diệt Heo xayda","Báo cáo với Quy Lão"},
        {"Đạt 200.000 sức mạnh","Đánh bọn Ốc mượn hồn lấy truyện","Báo cáo với Quy Lão"},
        {"Đạt 500.000 sức mạnh","Tiêu diệt Bulon","Tiêu diệt Ukulele","Tiêu diệt Quỷ mập","Báo cáo với Quy Lão"},
        {"Thách đấu thắng 10 người","Báo cáo với Quy Lão"},
        {"Đạt 1.500.000 sức mạnh","Tiêu diệt Akkuman","Tiêu diệt Tamborine","Tiêu diệt Drum","Báo cáo với Quy Lão"},
        {"Đạt 5.000.000 sức mạnh","Thắng vòng 2 đại hội võ thuật","Báo cáo với Quy Lão"},
        {"Đạt 15 triệu sức mạnh","Diệt Trung Úy Trắng(Trại Độc Nhãn)","Báo cáo với Quy Lão"},
        {"Đạt 50 triệu sức mạnh","Tiêu diệt Nappa","Tiêu diệt Soldier","Tiêu diệt Appule","Tiêu diệt Raspberry","Tiêu diệt Thằn lằn xanh","Báo cáo với Quy Lão"},
        {"Tiêu diệt Kuku","Tiêu diệt Mập Đầu Đinh","Tiêu diệt Rambo","Báo cáo với Quy Lão"},
        {"Tiêu diệt Số 4","Tiêu diệt Số 3","Tiêu diệt Số 1","Tiêu diệt Tiểu Đội Trưởng","Báo cáo với Quy Lão"},
        {"Tiêu diệt Fide cấp 1","Tiêu diệt Fide cấp 2","Tiêu diệt Fide cấp 3","Báo cáo với Quy Lão"},
        {"Báo cáo với ông Gohan","Đi tìm vị khách lạ","Đưa thuốc trợ tim cho Quy Lão","Đến tương lai gặp Bunma","Diệt Xên con cấp 1","Báo với Bunma tương lai"},
        {"Đến điểm hẹn tìm Rôbốt Sát Thủ","Tiêu diệt số 2 (Android 19)","Tiêu diệt số 1 (Android 20)","Diệt Xên con cấp 3","Báo với Bunma tương lai"},
        {"Đến sân sau siêu thị","Tiêu diệt Android 15","Tiêu diệt Android 14","Tiêu diệt Android 13","Báo với Bunma tương lai"},
        {"Đi tìm Píc Póc","Tiêu diệt Póc","Tiêu diệt Píc","Tiêu diệt King Kong","Diệt Xên con cấp 5","Báo với Bunma tương lai"},
        {"Đến thị trấn Ginder","Tiêu diệt Xên Bọ Hung cấp 1","Tiêu diệt Xên Bọ Hung cấp 2","Tiêu diệt Xên Bọ Hung hoàn thiện","Diệt Xên con cấp 8","Báo với Bunma tương lai"},
        {"Nâng sức đánh gốc lên 10K","Thu thập Capsule kì bí","Đến võ đài Xên Bọ Hung","Tiêu diệt 7 đứa con của Xên","Tiêu diệt Siêu Bọ Hung","Báo với Bunma tương lai"},
        {"Đi theo Ôsin","Hạ vua địa ngục Drabura","Hạ Pui Pui","Hạ Pui Pui lần 2","Hạ Yacôn","Hạ Drabura lần 2","Hạ Mabư","Báo cáo với Ôsin"},
        {"Tìm nhẫn thời không từ Super Black Goku","Sử dụng nhẫn thời không","Tìm người xayda đang bị thương","Hạ 5.000 Tobi và Cabira","Nói chuyện với Bardock","Tìm kiếm Berry đi lạc","Mang Berry về hang cho Bardock","Tìm 99 thức ăn cho Bardock tại bìa rừng","Hạ 10.000 Tobi và Cabira tại bìa rừng","Nói chuyện với Bardock"},
        {"Tìm nơi phi thuyền lạ vừa đáp","Giao chiến với tên trùm","Quay về hang gặp Berry gấp","Dùng thuốc mỡ trị thương","Hạ 20k Tobi, Cabira ở Rừng nguyên thủy","Hạ Chilled","Hạ Chilled cấp 2","Hạ Chilled bất kỳ 100 lần","Nói chuyện với Bardock"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
    };
    public String[][] subnameTASK1 = {
        {"Nhiệm vụ đầu tiên"},
        {"Đánh ngã 5 mộc nhân","Báo cáo với ông Morri"},
        {"Thu thập 10 đùi gà","Báo cáo với ông Morri"},
        {"Dùng tiềm năng","Tìm kiếm sao băng","Báo cáo với ông Morri"},
        {"Đánh 3 con lợn lòi mẹ","Đánh 3 con quỷ đất mẹ","Đánh 3 con khủng long mẹ","Báo cáo với ông Morri"},
        {"5"},
        {"6"},
        {"Đạt 16.000 sức mạnh","Đánh bại 20 con Phi long","Nói chuyện với Dende","Báo cáo với ông Morri"},
        {"Đạt 40.000 sức mạnh","Tìm viên ngọc rồng 7 sao","Đem ngọc về cho ông Morri","Tìm tháp Karin"},
        {"Tìm Trưởng Lão Guru tại Đảo Guru","Báo cáo với ông Morri"},
        {"10"},
        {"11"},
        {"Vào 1 bang hội","Báo cáo với Trưởng Lão Guru"},
        {"Tiêu diệt Heo rừng","Tiêu diệt Heo da xanh","Tiêu diệt Heo xayda","Báo cáo với Trưởng Lão Guru"},
        {"Đạt 200.000 sức mạnh","Đánh bọn Ốc sên lấy truyện","Báo cáo với Trưởng Lão Guru"},
        {"Đạt 500.000 sức mạnh","Tiêu diệt Bulon","Tiêu diệt Ukulele","Tiêu diệt Quỷ mập","Báo cáo với Trưởng Lão Guru"},
        {"Thách đấu thắng 10 người","Báo cáo với Trưởng Lão Guru"},
        {"Đạt 1.500.000 sức mạnh","Tiêu diệt Akkuman","Tiêu diệt Tamborine","Tiêu diệt Drum","Báo cáo với Trưởng Lão Guru"},
        {"Đạt 5.000.000 sức mạnh","Thắng vòng 2 đại hội võ thuật","Báo cáo với Trưởng Lão Guru"},
        {"Đạt 15 triệu sức mạnh","Diệt Trung Úy Trắng(Trại Độc Nhãn)","Báo cáo với Trưởng Lão Guru"},
        {"Đạt 50 triệu sức mạnh","Tiêu diệt Nappa","Tiêu diệt Soldier","Tiêu diệt Appule","Tiêu diệt Raspberry","Tiêu diệt Thằn lằn xanh","Báo cáo với Trưởng Lão Guru"},
        {"Tiêu diệt Kuku","Tiêu diệt Mập Đầu Đinh","Tiêu diệt Rambo","Báo cáo với Trưởng Lão Guru"},
        {"Tiêu diệt Số 4","Tiêu diệt Số 3","Tiêu diệt Số 1","Tiêu diệt Tiểu Đội Trưởng","Báo cáo với Trưởng Lão Guru"},
        {"Tiêu diệt Fide cấp 1","Tiêu diệt Fide cấp 2","Tiêu diệt Fide cấp 3","Báo cáo với Trưởng Lão Guru"},
        {"Báo cáo với ông Morri","Đi tìm vị khách lạ","Đưa thuốc trợ tim cho Quy Lão","Đến tương lai gặp Bunma","Diệt Xên con cấp 1","Báo với Bunma tương lai"},
        {"Đến điểm hẹn tìm Rôbốt Sát Thủ","Tiêu diệt số 2 (Android 19)","Tiêu diệt số 1 (Android 20)","Diệt Xên con cấp 3","Báo với Bunma tương lai"},
        {"Đến sân sau siêu thị","Tiêu diệt Android 15","Tiêu diệt Android 14","Tiêu diệt Android 13","Báo với Bunma tương lai"},
        {"Đi tìm Píc Póc","Tiêu diệt Póc","Tiêu diệt Píc","Tiêu diệt King Kong","Diệt Xên con cấp 5","Báo với Bunma tương lai"},
        {"Đến thị trấn Ginder","Tiêu diệt Xên Bọ Hung cấp 1","Tiêu diệt Xên Bọ Hung cấp 2","Tiêu diệt Xên Bọ Hung hoàn thiện","Diệt Xên con cấp 8","Báo với Bunma tương lai"},
        {"Nâng sức đánh gốc lên 10K","Thu thập Capsule kì bí","Đến võ đài Xên Bọ Hung","Tiêu diệt 7 đứa con của Xên","Tiêu diệt Siêu Bọ Hung","Báo với Bunma tương lai"},
        {"Đi theo Ôsin","Hạ vua địa ngục Drabura","Hạ Pui Pui","Hạ Pui Pui lần 2","Hạ Yacôn","Hạ Drabura lần 2","Hạ Mabư","Báo cáo với Ôsin"},
        {"Tìm nhẫn thời không từ Super Black Goku","Sử dụng nhẫn thời không","Tìm người xayda đang bị thương","Hạ 5.000 Tobi và Cabira","Nói chuyện với Bardock","Tìm kiếm Berry đi lạc","Mang Berry về hang cho Bardock","Tìm 99 thức ăn cho Bardock tại bìa rừng","Hạ 10.000 Tobi và Cabira tại bìa rừng","Nói chuyện với Bardock"},
        {"Tìm nơi phi thuyền lạ vừa đáp","Giao chiến với tên trùm","Quay về hang gặp Berry gấp","Dùng thuốc mỡ trị thương","Hạ 20k Tobi, Cabira ở Rừng nguyên thủy","Hạ Chilled","Hạ Chilled cấp 2","Hạ Chilled bất kỳ 100 lần","Nói chuyện với Bardock"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
    };
    public String[][] subnameTASK2 = {
        {"Nhiệm vụ đầu tiên"},
        {"Đánh ngã 5 mộc nhân","Báo cáo với ông Paragus"},
        {"Thu thập 10 đùi gà","Báo cáo với ông Paragus"},
        {"Dùng tiềm năng","Tìm kiếm sao băng","Báo cáo với ông Paragus"},
        {"Đánh 3 con quỷ đất mẹ","Đánh 3 con khủng long mẹ","Đánh 3 con lợn lòi mẹ","Báo cáo với ông Paragus"},
        {"5"},
        {"6"},
        {"Đạt 16.000 sức mạnh","Đánh bại 20 con Quỷ bay","Nói chuyện với Appule","Báo cáo với ông Paragus"},
        {"Đạt 40.000 sức mạnh","Tìm viên ngọc rồng 7 sao","Đem ngọc về cho ông Paragus","Tìm tháp Karin"},
        {"Tìm Vua Vegeta tại Vách Núi Đen","Báo cáo với ông Paragus"},
        {"10"},
        {"11"},
        {"Vào 1 bang hội","Báo cáo với Vua Vegeta"},
        {"Tiêu diệt Heo rừng","Tiêu diệt Heo da xanh","Tiêu diệt Heo xayda","Báo cáo với Vua Vegeta"},
        {"Đạt 200.000 sức mạnh","Đánh bọn Heo xayda mẹ lấy truyện","Báo cáo với Vua Vegeta"},
        {"Đạt 500.000 sức mạnh","Tiêu diệt Bulon","Tiêu diệt Ukulele","Tiêu diệt Quỷ mập","Báo cáo với Vua Vegeta"},
        {"Thách đấu thắng 10 người","Báo cáo với Vua Vegeta"},
        {"Đạt 1.500.000 sức mạnh","Tiêu diệt Akkuman","Tiêu diệt Tamborine","Tiêu diệt Drum","Báo cáo với Vua Vegeta"},
        {"Đạt 5.000.000 sức mạnh","Thắng vòng 2 đại hội võ thuật","Báo cáo với Vua Vegeta"},
        {"Đạt 15 triệu sức mạnh","Diệt Trung Úy Trắng(Trại Độc Nhãn)","Báo cáo với Vua Vegeta"},
        {"Đạt 50 triệu sức mạnh","Tiêu diệt Nappa","Tiêu diệt Soldier","Tiêu diệt Appule","Tiêu diệt Raspberry","Tiêu diệt Thằn lằn xanh","Báo cáo với Vua Vegeta"},
        {"Tiêu diệt Kuku","Tiêu diệt Mập Đầu Đinh","Tiêu diệt Rambo","Báo cáo với Vua Vegeta"},
        {"Tiêu diệt Số 4","Tiêu diệt Số 3","Tiêu diệt Số 1","Tiêu diệt Tiểu Đội Trưởng","Báo cáo với Vua Vegeta"},
        {"Tiêu diệt Fide cấp 1","Tiêu diệt Fide cấp 2","Tiêu diệt Fide cấp 3","Báo cáo với Vua Vegeta"},
        {"Báo cáo với ông Paragus","Đi tìm vị khách lạ","Đưa thuốc trợ tim cho Quy Lão","Đến tương lai gặp Bunma","Diệt Xên con cấp 1","Báo với Bunma tương lai"},
        {"Đến điểm hẹn tìm Rôbốt Sát Thủ","Tiêu diệt số 2 (Android 19)","Tiêu diệt số 1 (Android 20)","Diệt Xên con cấp 3","Báo với Bunma tương lai"},
        {"Đến sân sau siêu thị","Tiêu diệt Android 15","Tiêu diệt Android 14","Tiêu diệt Android 13","Báo với Bunma tương lai"},
        {"Đi tìm Píc Póc","Tiêu diệt Póc","Tiêu diệt Píc","Tiêu diệt King Kong","Diệt Xên con cấp 5","Báo với Bunma tương lai"},
        {"Đến thị trấn Ginder","Tiêu diệt Xên Bọ Hung cấp 1","Tiêu diệt Xên Bọ Hung cấp 2","Tiêu diệt Xên Bọ Hung hoàn thiện","Diệt Xên con cấp 8","Báo với Bunma tương lai"},
        {"Nâng sức đánh gốc lên 10K","Thu thập Capsule kì bí","Đến võ đài Xên Bọ Hung","Tiêu diệt 7 đứa con của Xên","Tiêu diệt Siêu Bọ Hung","Báo với Bunma tương lai"},
        {"Đi theo Ôsin","Hạ vua địa ngục Drabura","Hạ Pui Pui","Hạ Pui Pui lần 2","Hạ Yacôn","Hạ Drabura lần 2","Hạ Mabư","Báo cáo với Ôsin"},
        {"Tìm nhẫn thời không từ Super Black Goku","Sử dụng nhẫn thời không","Tìm người xayda đang bị thương","Hạ 5.000 Tobi và Cabira","Nói chuyện với Bardock","Tìm kiếm Berry đi lạc","Mang Berry về hang cho Bardock","Tìm 99 thức ăn cho Bardock tại bìa rừng","Hạ 10.000 Tobi và Cabira tại bìa rừng","Nói chuyện với Bardock"},
        {"Tìm nơi phi thuyền lạ vừa đáp","Giao chiến với tên trùm","Quay về hang gặp Berry gấp","Dùng thuốc mỡ trị thương","Hạ 20k Tobi, Cabira ở Rừng nguyên thủy","Hạ Chilled","Hạ Chilled cấp 2","Hạ Chilled bất kỳ 100 lần","Nói chuyện với Bardock"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
    };
    //CONTENT INFOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    public String[][] contentInfoTASK0 = {
        {"Nhiệm vụ đầu tiên"},
        {"Đánh ngã 5 mộc nhân","Báo cáo với ông Gohan"},
        {"Thu thập 10 đùi gà","Báo cáo với ông Gohan"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Gohan"},
        {"","Tiếp theo là Lợn lòi mẹ tại Namếc","Tiếp theo là Quỷ đất mẹ tại Xayda","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Gohan"},
        {"5"},
        {"6"},
        {"","","Hãy mau về làng nói chuyện với Bunma nào","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Gohan"},
        {"","","Tìm thấy viên ngọc rồng 7 sao rồi, đem về cho ông Gohan nào!",""},
        {"","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Gohan"},
        {"",""},
        {"",""},
        {"",""},
        {"Tiếp theo là Heo rừng","Tiếp theo là Heo da xanh","Tiếp theo là Heo xayda","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","Hãy mau tìm truyện Doremon tập 2","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","Tiếp theo là Bulon","Tiếp theo là Ukulele","Tiếp theo là Quỷ mập","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","Tiếp theo hãy tiêu diệt Akkuman","Tiếp theo hãy tiêu diệt Tamborine","Tiếp theo hãy tiêu diệt Drum","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"","Tiếp theo hãy tiêu diệt Nappa","Tiếp theo hãy tiêu diệt Soldier","Tiếp theo hãy tiêu diệt Appule","Tiếp theo hãy tiêu diệt Raspberry","Tiếp theo hãy tiêu diệt Thằn lằn xanh","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"Tiếp theo là Kuku","Tiếp theo là Mập Đầu Đinh","Tiếp theo là Rambo","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"Tiếp theo là Số 4","Tiếp theo là Số 3","Tiếp theo là Số 1","Tiếp theo là Tiểu Đội Trưởng","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"Tiếp theo là Fide cấp 1","Tiếp theo là Fide cấp 2","Tiếp theo là Fide cấp 3","Chúc mừng bạn, giờ hãy quay về báo cáo với Quy Lão"},
        {"Hãy về nhà báo cáo với ông Gohan","Hãy đi tìm vị khách lạ","Hãy đến đảo Kame đưa thuốc trợ tim cho Quy Lão","Mau đến tương lai gặp Bunma","Tiếp theo là diệt Xên con cấp 1","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến điểm hẹn tìm Rôbốt Sát Thủ","Tiếp theo là Android 19","Tiếp theo là Android 20","Tiếp theo là diệt Xên con cấp 3","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến sân sau siêu thị","Tiếp theo là Android 15","Tiếp theo là Android 14","Tiếp theo là Android 13","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đi tìm Píc Póc","Tiếp theo là Póc","Tiếp theo là Píc","Tiếp theo là King Kong","Tiếp theo là diệt Xên con cấp 5","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến thị trấn Ginder","Tiếp theo là Xên Bọ Hung cấp 1","Tiếp theo là Xên Bọ Hung cấp 2","Tiếp theo là Xên Bọ Hung hoàn thiện","Tiếp theo là diệt Xên con cấp 8","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Nâng sức đánh gốc lên 10K","Hãy thu thập Capsule kì bí","Mau đến võ đài Xên Bọ Hung","Tiếp theo là tiêu diệt 7 đứa con của Xên","Tiếp theo là Siêu Bọ Hung","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đi theo Ôsin","Tiếp theo là Drabura","Tiếp theo là Pui Pui","Tiếp tục tiêu diệt Pui Pui","Tiếp theo là Yacôn","Tiếp tục tiêu diệt Drabura","Tiếp theo là Mabư","Chúc mừng bạn, giờ hãy quay về báo cáo với Ôsin"},
        {"Tìm nhẫn thời không từ Super Black Goku","Sử dụng nhẫn thời không","Tìm người xayda đang bị thương","Tiêu diệt Tobi và Cabira","Nói chuyện với Bardock","Tìm kiếm Berry đi lạc","Mang Berry về hang cho Bardock","Tìm thức ăn cho Bardock","Tiếp tục tiêu diệt Tobi và Cabira","Chúc mừng bạn, giờ hãy quay về báo cáo với Bardock"},
        {"Tìm nơi phi thuyền lạ vừa đáp","Giao chiến với tên trùm","Quay về hang gặp Berry gấp","Dùng thuốc mỡ trị thương","Tiêu diệt Tobi, Cabira","Hạ Chilled","Hạ Chilled cấp 2","Tiếp tục hạ Chilled bất kỳ","Chúc mừng bạn, giờ hãy quay về báo cáo với Bardock"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
    };
    public String[][] contentInfoTASK1 = {
        {"Nhiệm vụ đầu tiên"},
        {"Đánh ngã 5 mộc nhân","Báo cáo với ông Morri"},
        {"Thu thập 10 đùi gà","Báo cáo với ông Morri"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Morri"},
        {"","Tiếp theo là Quỷ đất mẹ tại Xayda","Tiếp theo là Khủng long mẹ tại Trái Đất","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Morri"},
        {"5"},
        {"6"},
        {"","","Hãy mau về làng nói chuyện với Dende nào","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Morri"},
        {"","","Tìm thấy viên ngọc rồng 7 sao rồi, đem về cho ông Morri nào!",""},
        {"","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Morri"},
        {"",""},
        {"",""},
        {"",""},
        {"Tiếp theo là Heo rừng","Tiếp theo là Heo da xanh","Tiếp theo là Heo xayda","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","Hãy mau tìm truyện Doremon tập 2","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","Tiếp theo là Bulon","Tiếp theo là Ukulele","Tiếp theo là Quỷ mập","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","Tiếp theo hãy tiêu diệt Akkuman","Tiếp theo hãy tiêu diệt Tamborine","Tiếp theo hãy tiêu diệt Drum","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"","Tiếp theo hãy tiêu diệt Nappa","Tiếp theo hãy tiêu diệt Soldier","Tiếp theo hãy tiêu diệt Appule","Tiếp theo hãy tiêu diệt Raspberry","Tiếp theo hãy tiêu diệt Thằn lằn xanh","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"Tiếp theo là Kuku","Tiếp theo là Mập Đầu Đinh","Tiếp theo là Rambo","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"Tiếp theo là Số 4","Tiếp theo là Số 3","Tiếp theo là Số 1","Tiếp theo là Tiểu Đội Trưởng","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"Tiếp theo là Fide cấp 1","Tiếp theo là Fide cấp 2","Tiếp theo là Fide cấp 3","Chúc mừng bạn, giờ hãy quay về báo cáo với Trưởng Lão Guru"},
        {"Hãy về nhà báo cáo với ông Morri","Hãy đi tìm vị khách lạ","Hãy đến đảo Kame đưa thuốc trợ tim cho Quy Lão","Mau đến tương lai gặp Bunma","Tiếp theo là diệt Xên con cấp 1","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến điểm hẹn tìm Rôbốt Sát Thủ","Tiếp theo là Android 19","Tiếp theo là Android 20","Tiếp theo là diệt Xên con cấp 3","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến sân sau siêu thị","Tiếp theo là Android 15","Tiếp theo là Android 14","Tiếp theo là Android 13","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đi tìm Píc Póc","Tiếp theo là Póc","Tiếp theo là Píc","Tiếp theo là King Kong","Tiếp theo là diệt Xên con cấp 5","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến thị trấn Ginder","Tiếp theo là Xên Bọ Hung cấp 1","Tiếp theo là Xên Bọ Hung cấp 2","Tiếp theo là Xên Bọ Hung hoàn thiện","Tiếp theo là diệt Xên con cấp 8","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Nâng sức đánh gốc lên 10K","Hãy thu thập Capsule kì bí","Mau đến võ đài Xên Bọ Hung","Tiếp theo là tiêu diệt 7 đứa con của Xên","Tiếp theo là Siêu Bọ Hung","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đi theo Ôsin","Tiếp theo là Drabura","Tiếp theo là Pui Pui","Tiếp tục tiêu diệt Pui Pui","Tiếp theo là Yacôn","Tiếp tục tiêu diệt Drabura","Tiếp theo là Mabư","Chúc mừng bạn, giờ hãy quay về báo cáo với Ôsin"},
        {"Tìm nhẫn thời không từ Super Black Goku","Sử dụng nhẫn thời không","Tìm người xayda đang bị thương","Tiêu diệt Tobi và Cabira","Nói chuyện với Bardock","Tìm kiếm Berry đi lạc","Mang Berry về hang cho Bardock","Tìm thức ăn cho Bardock","Tiếp tục tiêu diệt Tobi và Cabira","Chúc mừng bạn, giờ hãy quay về báo cáo với Bardock"},
        {"Tìm nơi phi thuyền lạ vừa đáp","Giao chiến với tên trùm","Quay về hang gặp Berry gấp","Dùng thuốc mỡ trị thương","Tiêu diệt Tobi, Cabira","Hạ Chilled","Hạ Chilled cấp 2","Tiếp tục hạ Chilled bất kỳ","Chúc mừng bạn, giờ hãy quay về báo cáo với Bardock"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
    };
    public String[][] contentInfoTASK2 = {
        {"Nhiệm vụ đầu tiên"},
        {"Đánh ngã 5 mộc nhân","Báo cáo với ông Paragus"},
        {"Thu thập 10 đùi gà","Báo cáo với ông Paragus"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Paragus"},
        {"","Tiếp theo là Khủng long mẹ tại Trái Đất","Tiếp theo là Lợn lòi mẹ tại Namếc","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Paragus"},
        {"5"},
        {"6"},
        {"","","Hãy mau về làng nói chuyện với Appule nào","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Paragus"},
        {"","","Tìm thấy viên ngọc rồng 7 sao rồi, đem về cho ông Paragus nào!",""},
        {"","Chúc mừng bạn, giờ hãy quay về báo cáo với ông Paragus"},
        {"",""},
        {"",""},
        {"",""},
        {"Tiếp theo là Heo rừng","Tiếp theo là Heo da xanh","Tiếp theo là Heo xayda","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","Hãy mau tìm truyện Doremon tập 2","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","Tiếp theo là Bulon","Tiếp theo là Ukulele","Tiếp theo là Quỷ mập","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","Tiếp theo hãy tiêu diệt Akkuman","Tiếp theo hãy tiêu diệt Tamborine","Tiếp theo hãy tiêu diệt Drum","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"","Tiếp theo hãy tiêu diệt Nappa","Tiếp theo hãy tiêu diệt Soldier","Tiếp theo hãy tiêu diệt Appule","Tiếp theo hãy tiêu diệt Raspberry","Tiếp theo hãy tiêu diệt Thằn lằn xanh","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"Tiếp theo là Kuku","Tiếp theo là Mập Đầu Đinh","Tiếp theo là Rambo","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"Tiếp theo là Số 4","Tiếp theo là Số 3","Tiếp theo là Số 1","Tiếp theo là Tiểu Đội Trưởng","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"Tiếp theo là Fide cấp 1","Tiếp theo là Fide cấp 2","Tiếp theo là Fide cấp 3","Chúc mừng bạn, giờ hãy quay về báo cáo với Vua Vegeta"},
        {"Hãy về nhà báo cáo với ông Paragus","Hãy đi tìm vị khách lạ","Hãy đến đảo Kame đưa thuốc trợ tim cho Quy Lão","Mau đến tương lai gặp Bunma","Tiếp theo là diệt Xên con cấp 1","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến điểm hẹn tìm Rôbốt Sát Thủ","Tiếp theo là Android 19","Tiếp theo là Android 20","Tiếp theo là diệt Xên con cấp 3","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến sân sau siêu thị","Tiếp theo là Android 15","Tiếp theo là Android 14","Tiếp theo là Android 13","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đi tìm Píc Póc","Tiếp theo là Póc","Tiếp theo là Píc","Tiếp theo là King Kong","Tiếp theo là diệt Xên con cấp 5","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đến thị trấn Ginder","Tiếp theo là Xên Bọ Hung cấp 1","Tiếp theo là Xên Bọ Hung cấp 2","Tiếp theo là Xên Bọ Hung hoàn thiện","Tiếp theo là diệt Xên con cấp 8","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Nâng sức đánh gốc lên 10K","Hãy thu thập Capsule kì bí","Mau đến võ đài Xên Bọ Hung","Tiếp theo là tiêu diệt 7 đứa con của Xên","Tiếp theo là Siêu Bọ Hung","Chúc mừng bạn, giờ hãy quay về báo cáo với Bunma tương lai"},
        {"Đi theo Ôsin","Tiếp theo là Drabura","Tiếp theo là Pui Pui","Tiếp tục tiêu diệt Pui Pui","Tiếp theo là Yacôn","Tiếp tục tiêu diệt Drabura","Tiếp theo là Mabư","Chúc mừng bạn, giờ hãy quay về báo cáo với Ôsin"},
        {"Tìm nhẫn thời không từ Super Black Goku","Sử dụng nhẫn thời không","Tìm người xayda đang bị thương","Tiêu diệt Tobi và Cabira","Nói chuyện với Bardock","Tìm kiếm Berry đi lạc","Mang Berry về hang cho Bardock","Tìm thức ăn cho Bardock","Tiếp tục tiêu diệt Tobi và Cabira","Chúc mừng bạn, giờ hãy quay về báo cáo với Bardock"},
        {"Tìm nơi phi thuyền lạ vừa đáp","Giao chiến với tên trùm","Quay về hang gặp Berry gấp","Dùng thuốc mỡ trị thương","Tiêu diệt Tobi, Cabira","Hạ Chilled","Hạ Chilled cấp 2","Tiếp tục hạ Chilled bất kỳ","Chúc mừng bạn, giờ hãy quay về báo cáo với Bardock"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
        {"Chưa có nhiệm vụ mới","Nhiệm vụ mới đang được cập nhật"},
    };
    //NPC TASKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
    public byte[][] npcTASK0 = {
        {0},
        {-1,0},
        {-1,0},
        {-1,-1,0},
        {-1,-1,-1,0},
        {-1},
        {-1},
        {-1,-1,7,0},
        {-1,-1,0,-1},
        {13,0},
        {-1,-1},
        {-1,-1},
        {-1,13}, //nv vao bang hoi
        {-1,-1,-1,13},
        {-1,-1,13},
        {-1,-1,-1,-1,13},
        {-1,13},
        {-1,-1,-1,-1,13},
        {-1,-1,13},
        {-1,-1,13},
        {-1,-1,-1,-1,-1,-1,13}, //nv bat kha thi
        {-1,-1,-1,13},
        {-1,-1,-1,-1,13}, //tdst
        {-1,-1,-1,13}, //fide
        {0,38,13,37,-1,37}, //NV xen 1
        {-1,-1,-1,-1,37},
        {-1,-1,-1,-1,37},
        {-1,-1,-1,-1,-1,37}, //picpockingkong
        {-1,-1,-1,-1,-1,37},
        {18,-1,-1,-1,-1,37}, //nv sieu bo hung
        {44,-1,-1,-1,-1,-1,-1,44},
        {-1,-1,70,-1,70,71,70,70,-1,70},
        {-1,-1,71,-1,-1,-1,-1,-1,70},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public byte[][] npcTASK1 = {
        {0},
        {-1,2},
        {-1,2},
        {-1,-1,2},
        {-1,-1,-1,2},
        {-1},
        {-1},
        {-1,-1,8,2},
        {-1,-1,2,-1},
        {14,2},
        {-1,-1},
        {-1,-1},
        {-1,14},
        {-1,-1,-1,14},
        {-1,-1,14},
        {-1,-1,-1,-1,14},
        {-1,14},
        {-1,-1,-1,-1,14},
        {-1,-1,14},
        {-1,-1,14},
        {-1,-1,-1,-1,-1,-1,14}, //nv bat kha thi
        {-1,-1,-1,14},
        {-1,-1,-1,-1,14}, //tdst
        {-1,-1,-1,14}, //fide
        {2,38,13,37,-1,37}, //NV xen 1
        {-1,-1,-1,-1,37},
        {-1,-1,-1,-1,37},
        {-1,-1,-1,-1,-1,37}, //picpockingkong
        {-1,-1,-1,-1,-1,37},
        {18,-1,-1,-1,-1,37}, //nv sieu bo hung
        {44,-1,-1,-1,-1,-1,-1,44},
        {-1,-1,70,-1,70,71,70,70,-1,70},
        {-1,-1,71,-1,-1,-1,-1,-1,70},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public byte[][] npcTASK2 = {
        {0},
        {-1,1},
        {-1,1},
        {-1,-1,1},
        {-1,-1,-1,1},
        {-1},
        {-1},
        {-1,-1,9,1},
        {-1,-1,1,-1},
        {15,1},
        {-1,-1},
        {-1,-1},
        {-1,15},
        {-1,-1,-1,15},
        {-1,-1,15},
        {-1,-1,-1,-1,15},
        {-1,15},
        {-1,-1,-1,-1,15},
        {-1,-1,15},
        {-1,-1,15},
        {-1,-1,-1,-1,-1,-1,15}, //nv bat kha thi
        {-1,-1,-1,15},
        {-1,-1,-1,-1,15}, //tdst
        {-1,-1,-1,15}, //fide
        {1,38,13,37,-1,37}, //NV xen 1
        {-1,-1,-1,-1,37},
        {-1,-1,-1,-1,37},
        {-1,-1,-1,-1,-1,37}, //picpockingkong
        {-1,-1,-1,-1,-1,37},
        {18,-1,-1,-1,-1,37}, //nv sieu bo hung
        {44,-1,-1,-1,-1,-1,-1,44},
        {-1,-1,70,-1,70,71,70,70,-1,70},
        {-1,-1,71,-1,-1,-1,-1,-1,70},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    //MAP TASKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
    public short[][] mapTASK0 = {
        {0},
        {0,21},
        {1,21},
        {-1,42,21},
        {1,8,15,21},
        {-1},
        {-1},
        {-1,3,0,21},
        {-1,12,21,47},
        {5,21},
        {-1},
        {-1},
        {-1,5},
        {28,32,36,5},
        {-1,-1,5},
        {-1,30,34,38,5},
        {-1,5},
        {-1,19,6,10,5},
        {-1,52,5},
        {-1,59,5},
        {-1,68,69,70,71,72,5},
        {68,64,73,5},
        {83,83,83,83,5}, //tdst
        {80,80,80,5}, //fide
        {21,27,5,102,92,102}, //NV xen 1
        {93,93,93,94,102},
        {104,104,104,104,102},
        {97,97,97,97,97,102}, //picpockingkong
        {100,100,100,100,100,102},
        {46,-1,103,103,103,102}, //nv sieu bo hung
        {52,114,115,117,118,119,120,52},
        {92,-1,160,160,160,161,160,160,161,160},
        {163,163,161,-1,162,163,163,163,160},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public short[][] mapTASK1 = {
        {0},
        {7,22},
        {8,22},
        {-1,43,22},
        {8,15,1,22},
        {-1},
        {-1},
        {-1,11,7,22},
        {-1,18,22,47},
        {13,22},
        {-1},
        {-1},
        {-1,13},
        {28,32,36,13},
        {-1,-1,13},
        {-1,30,34,38,13},
        {-1,13},
        {-1,19,6,10,13},
        {-1,52,13},
        {-1,59,13},
        {-1,68,69,70,71,72,13},
        {68,64,73,13},
        {83,83,83,83,13}, //tdst
        {80,80,80,13}, //fide
        {22,27,5,102,92,102}, //NV xen 1
        {93,93,93,94,102},
        {104,104,104,104,102},
        {97,97,97,97,97,102}, //picpockingkong
        {100,100,100,100,100,102},
        {46,-1,103,103,103,102}, //nv sieu bo hung
        {52,114,115,117,118,119,120,52},
        {92,-1,160,160,160,161,160,160,161,160},
        {163,163,161,-1,162,163,163,163,160},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public short[][] mapTASK2 = {
        {0},
        {14,23},
        {15,23},
        {-1,44,23},
        {15,1,8,23},
        {-1},
        {-1},
        {-1,17,14,23},
        {-1,4,23,47},
        {20,23},
        {-1},
        {-1},
        {-1,20},
        {28,32,36,20},
        {-1,-1,20},
        {-1,30,34,38,20},
        {-1,20},
        {-1,19,6,10,20},
        {-1,52,20},
        {-1,59,20},
        {-1,68,69,70,71,72,20},
        {68,64,73,20},
        {83,83,83,83,20}, //tdst
        {80,80,80,20}, //fide
        {23,27,5,102,92,102}, //NV xen 1
        {93,93,93,94,102},
        {104,104,104,104,102},
        {97,97,97,97,97,102}, //picpockingkong
        {100,100,100,100,100,102},
        {46,-1,103,103,103,102}, //nv sieu bo hung
        {52,114,115,117,118,119,120,52},
        {92,-1,160,160,160,161,160,160,161,160},
        {163,163,161,-1,162,163,163,163,160},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public int[][] mobTASK0 = {
        {0},
        {0,-1},
        {1,-1},
        {-1,-1,-1},
        {4,5,6,-1},
        {-1},
        {-1},
        {-1,7,-1,-1},
        {-1,11,-1,-1}, //10 than lan me
        {-1,-1},
        {-1},
        {-1},
        {-1,-1},
        {16,17,18,-1},
        {-1,13,-1},
        {-1,22,23,24,-1},
        {-1,-1},
        {-1,27,25,26,-1},
        {-1,-1,-1},
        {-1,3100,-1},
        {-1,39,40,41,42,43,-1},
        {700,800,900,-1}, //NHIEM VU TIEU DIET DE TU FIDE //TYPE BOSS
        {1000,1100,1300,1400,-1}, //tdst //TYPE BOSS
        {1500,1600,1700,-1}, //fide
        {-1,-1,-1,-1,58,-1}, //NV xen 1
        {-1,1800,1900,60,-1}, //nv xen 3
        {-1,2000,2100,2200,-1},
        {-1,2300,2400,2500,62,-1}, //picpockingkong
        {-1,2600,2700,2800,65,-1}, //xen
        {-1,-1,-1,2900,3000,-1}, //nv sieu bo hung
        {-1,3600,3700,3700,3800,3600,3900,-1},
        {-1,-1,-1,81,-1,-1,-1,-1,81,-1},
        {-1,4000,-1,-1,81,4000,4100,4000,-1},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public int[][] mobTASK1 = {
        {0},
        {0,-1},
        {2,-1},
        {-1,-1,-1},
        {5,6,4,-1},
        {-1},
        {-1},
        {-1,8,-1,-1},
        {-1,12,-1,-1}, //11 phi long me
        {-1,-1},
        {-1},
        {-1},
        {-1,-1},
        {16,17,18,-1},
        {-1,14,-1},
        {-1,22,23,24,-1},
        {-1,-1}, //nvthachdau
        {-1,27,25,26,-1},
        {-1,-1,-1},
        {-1,3100,-1},
        {-1,39,40,41,42,43,-1},
        {700,800,900,-1}, //NHIEM VU TIEU DIET DE TU FIDE //TYPE BOSS
        {1000,1100,1300,1400,-1}, //tdst //TYPE BOSS
        {1500,1600,1700,-1}, //fide
        {-1,-1,-1,-1,58,-1}, //NV xen 1
        {-1,1800,1900,60,-1}, //nv xen 3
        {-1,2000,2100,2200,-1},
        {-1,2300,2400,2500,62,-1}, //picpockingkong
        {-1,2600,2700,2800,65,-1}, //xen
        {-1,-1,-1,2900,3000,-1}, //nv sieu bo hung
        {-1,3600,3700,3700,3800,3600,3900,-1},
        {-1,-1,-1,81,-1,-1,-1,-1,81,-1},
        {-1,4000,-1,-1,81,4000,4100,4000,-1},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public int[][] mobTASK2 = {
        {0},
        {0,-1},
        {3,-1},
        {-1,-1,-1},
        {6,4,5,-1},
        {-1},
        {-1},
        {-1,9,-1,-1},
        {-1,10,-1,-1}, //10 than lan me
        {-1,-1},
        {-1},
        {-1},
        {-1,-1},
        {16,17,18,-1},
        {-1,15,-1},
        {-1,22,23,24,-1},
        {-1,-1},
        {-1,27,25,26,-1},
        {-1,-1,-1},
        {-1,3100,-1},
        {-1,39,40,41,42,43,-1},
        {700,800,900,-1}, //NHIEM VU TIEU DIET DE TU FIDE //TYPE BOSS
        {1000,1100,1300,1400,-1}, //tdst //TYPE BOSS
        {1500,1600,1700,-1}, //fide
        {-1,-1,-1,-1,58,-1}, //NV xen 1
        {-1,1800,1900,60,-1}, //nv xen 3
        {-1,2000,2100,2200,-1},
        {-1,2300,2400,2500,62,-1}, //picpockingkong
        {-1,2600,2700,2800,65,-1}, //xen
        {-1,-1,-1,2900,3000,-1}, //nv sieu bo hung
        {-1,3600,3700,3700,3800,3600,3900,-1},
        {-1,-1,-1,81,-1,-1,-1,-1,81,-1},
        {-1,4000,-1,-1,81,4000,4100,4000,-1},
        {-1,-1},
        {-1,-1},
        {-1,-1},
    };
    public String[] textChatTASK = {
        "NV DAUTIEN",
        "Tốt lắm, giờ con hãy đi thu thập đùi gà cho ta",
        "Đùi gà đây rồi, tốt lắm, haha. Ta sẽ nướng tại đống lửa đằng kia, con có thể ăn bất cứ lúc nào nếu muốn",
        "Có em bé trong phi thuyền rơi xuống à, ta cứ tưởng là sao băng",
        "Tốt lắm, con hoàn thành rất xuất xắc",
        "",
        "",
        "Tốt lắm, con làm ta bất ngờ đấy. Bây giờ hãy đi lấy lại ngọc đem về đây cho ta",
        "Xong nhiệm vụ tìm ngọc rồng 7 sao",
        "Ta không nhận đệ tử đâu. Ồ con tặng ta truyện Doremon hả, thôi được nhưng con phải cố gắng luyện tập đó nhé. Hãy gia nhập một bang hội để luyện tập, sau đó quay lại đây gặp ta",
        "",
        "",
        "Con hãy cùng các thành viên trong bang tiêu diệt cho ta 30 con Heo rừng, 30 con Heo da xanh và 30 con Heo Xayda",
        "Bang của con rất có tinh thần đồng đội, con hãy cùng các thành viên luyện tập chăm chỉ để thành tài nhé",
        "Con đã tìm thấy truyện Doremon tập 2 rồi à, mau đưa cho ta nào",
        "Con và bang hội làm rất tốt, ta có quà dành cho con", //XONG NV BULON
        "Con làm rất tốt, ta có quà dành cho con", //XONG NV THACH DAU
        "Con làm rất tốt, ta có quà dành cho con", //XONG NV TIEU DIET BOSS TRUM
        "Con làm rất tốt, ta có quà dành cho con", //XONG NV DAI HOI VO THUAT
        "Con làm rất tốt, Trung Úy Trắng đã bị tiêu diệt. Ta có quà dành cho con", //XONG NV TRUNG UY TRANG
        "Tốt lắm, giờ con hãy đi tiêu diệt lũ đệ tử của Fide cho ta", //XONG NV BAT KHA THI
        "Con làm rất tốt, ta có quà dành cho con", //XONG NV TIEU DIET DE TU FIDE
        "Rất tốt, bọn Fide đã biết sức mạnh của chúng ta", //XONG NV TIEU DIET DE TU FIDE
        "Rất tốt, bọn Fide đã biết sức mạnh thật sự của chúng ta", //XONG NV FIDE
        "Cảm ơn cậu đã giải vây cho chúng tôi\nHãy đến thành phố phía nam, đảo balê hoặc cao nguyên tìm và chặn đánh 2 Rôbốt Sát Thủ\nCẩn thận vì bọn chúng rất mạnh", //XONG NV Xen1
        "Số 1 chính là bác học Kôrê\nÔng ta đã tự biến mình thành Rôbốt để được bất tử\n2 tên Rôbốt này không phải là Rôbốt sát thủ mà chúng tôi nói đến\nCó thể quá khứ đã thay đổi từ khi cậu đến đây\nMau trở về quá khứ xem chuyện gì đã xảy ra",
        "Bác học Kôrê thật sự là thiên tài\nCả máy tính của ông ta cũng có thể\ntự động tạo ra Rôbốt sát thủ\n2 đứa Rôbốt sát thủ mà chúng tôi nói\ncõ 17, 18 tuổi, 1 trai 1 gái ăn mặc như cao bồi\nBề ngoài thấy hiền lành nhưng ra tay cực kì tàn độc\nCậu phải cẩn thận đừng khinh địch.",
        "Tôi và Ca Lích vừa phát hiện ra 1 vỏ trứng kì lạ đã nở\nGần đó còn có vỏ của một con ve sầu rất to vừa lột xác\nCậu hãy đến thị trấn Ginder tọa độ 213-xyz xem thử\nTôi nghi ngờ nó là 1 tác phẩm nữa của lão Kôrê\nCậu cầm lấy cái này, đó là rađa rò tìm Capsule kì bí\nChúc cậu tìm được vật gì đó thú vị",
        "Hắn sợ chúng ta quá nên bày trò câu giờ đây mà\nCậu hãy tranh thủ 3 ngày này tập luyện để nâng cao sức mạnh bản thân nhé\nCapsule kì bí không chừng lại có ích\nHãy thu thập 1 ít để phòng thân",
        "Chúc mừng cậu đã chiến thắng Siêu Bọ Hung\nCám ơn cậu rất nhiều\nnếu rảnh rỗi cậu hãy đến đây tìm Capsule kì bí nhá",
        "Mabư cuối cùng cũng đã bị tiêu diệt, hòa bình đã đến với toàn cõi vũ trụ, cậu đúng là cứu tinh của nhân loại.",
        "OK bạn",
        "OK bạn",
        "Chưa có nhiệm vụ mới",
        "Chưa có nhiệm vụ mới",
        "Chưa có nhiệm vụ mới",
    };
    //APPULE Cảm ơn ngươi đã cứu ta. Ta sẽ sẵn sàng phục vụ nếu ngươi cần mua vật\ndụng
    private static TaskManager instance;

    public static TaskManager gI() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void init() {
        Util.log("Load list task");
        Task _task = null;
        //TASK 0
        _task = new Task();
        _task.taskId = (short)0;
        _task.name = "Nhiệm vụ đầu tiên";
        tasks.add(_task);
        //TASK MOC NHAN
        _task = new Task();
        _task.taskId = (short)1;
        _task.name = "Nhiệm vụ tập luyện";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)5;
        _task.counts[1] = (short)-1;
        _task.bonus = 500;
        tasks.add(_task);
        //THU THAP DUI GA
        _task = new Task();
        _task.taskId = (short)2;
        _task.name = "Nhiệm vụ thử thách";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)10;
        _task.counts[1] = (short)-1;
        _task.bonus = 1000;
        tasks.add(_task);
        //TIM SAO BANG
        _task = new Task();
        _task.taskId = (short)3;
        _task.name = "Nhiệm vụ sao băng";
        _task.countSub = (byte)3;
        _task.counts = new short[3];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.bonus = 2000;
        tasks.add(_task);
        //TIEU DIET LON LOI ME
        _task = new Task();
        _task.taskId = (short)4;
        _task.name = "Nhiệm vụ";
        _task.countSub = (byte)4;
        _task.counts = new short[4];
        _task.counts[0] = (short)3;
        _task.counts[1] = (short)3;
        _task.counts[2] = (short)3;
        _task.counts[3] = (short)-1;
        _task.bonus = 4000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)5;
        _task.name = "Nhiệm vụ 5";
        tasks.add(_task);
        _task = new Task();
        _task.taskId = (short)6;
        _task.name = "Nhiệm vụ 6";
        tasks.add(_task);
        //GIAI CUU APULE
        _task = new Task();
        _task.taskId = (short)7;
        _task.name = "Nhiệm vụ giải cứu";
        _task.countSub = (byte)4;
        _task.counts = new short[4];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)20;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.bonus = 8000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)8;
        _task.name = "Nhiệm vụ tìm ngọc";
        _task.countSub = (byte)4;
        _task.counts = new short[4];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.bonus = 15000;
        tasks.add(_task);
        
        _task = new Task();
        _task.taskId = (short)9;
        _task.name = "Nhiệm vụ bái sư";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.bonus = 5000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)10;
        _task.name = "Nhiệm vụ 10";
        tasks.add(_task);
        _task = new Task();
        _task.taskId = (short)11;
        _task.name = "Nhiệm vụ 11";
        tasks.add(_task);
        
        _task = new Task();
        _task.taskId = (short)12;
        _task.name = "Nhiệm vụ gia nhập bang hội";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.bonus = 20000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)13;
        _task.name = "Nhiệm Vụ bang hội đầu tiên";
        _task.countSub = (byte)4;
        _task.counts = new short[4];
        _task.counts[0] = (short)30;
        _task.counts[1] = (short)30;
        _task.counts[2] = (short)30;
        _task.counts[3] = (short)-1;
        _task.bonus = 50000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)14;
        _task.name = "Nhiệm vụ bái sư";
        _task.countSub = (byte)3;
        _task.counts = new short[3];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.bonus = 80000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)15;
        _task.name = "Nhiệm vụ bang hội thứ 2";
        _task.countSub = (byte)5;
        _task.counts = new short[5];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)30;
        _task.counts[2] = (short)30;
        _task.counts[3] = (short)30;
        _task.counts[4] = (short)-1;
        _task.bonus = 150000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)16;
        _task.name = "Nhiệm vụ thách đấu";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)10;
        _task.counts[1] = (short)-1;
        _task.bonus = 150000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)17;
        _task.name = "Nhiệm vụ tiêu diệt Boss Trùm";
        _task.countSub = (byte)5;
        _task.counts = new short[5];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)-1;
        _task.bonus = 200000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)18;
        _task.name = "Nhiệm vụ thử thách";
        _task.countSub = (byte)3;
        _task.counts = new short[3];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.bonus = 500000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)19;
        _task.name = "Nhiệm vụ cam go";
        _task.countSub = (byte)3;
        _task.counts = new short[3];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.bonus = 5000000;
        tasks.add(_task);


        //NHIEM VU FIDE
        _task = new Task();
        _task.taskId = (short)20;
        _task.name = "Nhiệm vụ bất khả thi";
        _task.countSub = (byte)7;
        _task.counts = new short[7];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)500;
        _task.counts[2] = (short)400;
        _task.counts[3] = (short)300;
        _task.counts[4] = (short)200;
        _task.counts[5] = (short)100;
        _task.counts[6] = (short)-1;
        _task.bonus = 50000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)21;
        _task.name = "Nhiệm vụ tìm diệt đệ tử";
        _task.countSub = (byte)4;
        _task.counts = new short[4];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.bonus = 20000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)22;
        _task.name = "Tiểu đội sát thủ";
        _task.countSub = (byte)5;
        _task.counts = new short[5];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)-1;
        _task.bonus = 20000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)23;
        _task.name = "Fide đại ca";
        _task.countSub = (byte)4;
        _task.counts = new short[4];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.bonus = 20000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)24;
        _task.name = "Chú bé đến từ tương lai";
        _task.countSub = (byte)6;
        _task.counts = new short[6];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)1000;
        _task.counts[5] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)25;
        _task.name = "Chạm trán Rôbốt Sát Thủ lần 1";
        _task.countSub = (byte)5;
        _task.counts = new short[5];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)900;
        _task.counts[4] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)26;
        _task.name = "Chạm trán Rôbốt Sát Thủ lần 2";
        _task.countSub = (byte)5;
        _task.counts = new short[5];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)27;
        _task.name = "Chạm trán Rôbốt Sát Thủ lần 3";
        _task.countSub = (byte)6;
        _task.counts = new short[6];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)800;
        _task.counts[5] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)28;
        _task.name = "Chạm trán Xên bọ hung";
        _task.countSub = (byte)6;
        _task.counts = new short[6];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)700;
        _task.counts[5] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)29;
        _task.name = "Cuộc dạo chơi của Xên";
        _task.countSub = (byte)6;
        _task.counts = new short[6];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)50;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)7;
        _task.counts[4] = (short)-1;
        _task.counts[5] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);

        _task = new Task();
        _task.taskId = (short)30;
        _task.name = "Cuộc đối đầu không cân sức";
        _task.countSub = (byte)8;
        _task.counts = new short[8];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)10;
        _task.counts[2] = (short)10;
        _task.counts[3] = (short)10;
        _task.counts[4] = (short)10;
        _task.counts[5] = (short)10;
        _task.counts[6] = (short)10;
        _task.counts[7] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);
        
        _task = new Task();
        _task.taskId = (short)31;
        _task.name = "Truyền thuyết về Siêu Xayda Huyền Thoại";
        _task.countSub = (byte)10;
        _task.counts = new short[10];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)5000;
        _task.counts[4] = (short)-1;
        _task.counts[5] = (short)-1;
        _task.counts[6] = (short)-1;
        _task.counts[7] = (short)-1;
        _task.counts[8] = (short)10000;
        _task.counts[9] = (short)-1;
        _task.bonus = 10000000;
        tasks.add(_task);
        _task = new Task();
        _task.taskId = (short)32;
        _task.name = "Chạm trán người ngoài hành tinh";
        _task.countSub = (byte)9;
        _task.counts = new short[9];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.counts[2] = (short)-1;
        _task.counts[3] = (short)-1;
        _task.counts[4] = (short)20000;
        _task.counts[5] = (short)-1;
        _task.counts[6] = (short)-1;
        _task.counts[7] = (short)100;
        _task.counts[8] = (short)-1;
        _task.bonus = 10000000;
        tasks.add(_task);
        _task = new Task();
        _task.taskId = (short)33;
        _task.name = "Chưa có nhiệm vụ mới";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);
        _task = new Task();
        _task.taskId = (short)34;
        _task.name = "Chưa có nhiệm vụ mới";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);
        _task = new Task();
        _task.taskId = (short)35;
        _task.name = "Chưa có nhiệm vụ mới";
        _task.countSub = (byte)2;
        _task.counts = new short[2];
        _task.counts[0] = (short)-1;
        _task.counts[1] = (short)-1;
        _task.bonus = 1000000;
        tasks.add(_task);
        Util.log("Finish load list task: " + tasks.size());
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Task getTaskById(short id) {
        for (Task task : tasks) {
            if (task.taskId == id) {
                return task;
            }
        }
        return null;
    }
}
