package express;


import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

public class List {
    private final LinkedList<Express> list;

    public List() {
        list = new LinkedList<>();
    }

    public  LinkedList<Express> getList() {
        return list;
    }

    public void add(Express data) {
        list.add(data);
    }

    public Boolean remove(String waybillNumber) {
        for (Express data : list) {
            if (data.getWaybillNumber().equals(waybillNumber)) {
                list.remove(data);
                return true;
            }
        }
        return false;
    }

    public Express search(String waybillNumber) {
        for (Express data : list) {
            if (data.getWaybillNumber().equals(waybillNumber)) {
                return data;
            }
        }
        return null;
    }

    private void printExpress(Express data) {
        System.out.println("运单号：");
        System.out.println(data.getWaybillNumber());
        System.out.println("运单类型：");
        System.out.println(data.getWaybillType());
        System.out.println("寄件人信息：");
        System.out.println("地址：" + data.getSender().getAddress());
        System.out.println("电话：" + data.getSender().getPhone());
        System.out.println("备注：" + data.getSender().getRemark());
        System.out.println("寄件时间：" + data.getSender().getTime().getTime());
        System.out.println("收件人信息：");
        System.out.println("地址：" + data.getReceiver().getAddress());
        System.out.println("电话：" + data.getReceiver().getPhone());
        System.out.println("备注：" + data.getReceiver().getRemark());
        System.out.println("收件时间：" + data.getReceiver().getTime().getTime());
        System.out.println("是否签收：" + data.getIsSign());
        System.out.println("是否为难寄快递：" + data.getIsDifficult());
        System.out.println("难寄快递原因：" + data.getDifficultReason());
        System.out.println("金额：" + data.getAmount());
    }

    public void print() {
        for (Express data : list) {
            printExpress(data);
        }
    }

    public void update(Express data) {
        for (Express e : list) {
            if (e.getWaybillNumber().equals(data.getWaybillNumber())) {
                e.setWaybillType(data.getWaybillType());
                e.setSender(data.getSender());
                e.setReceiver(data.getReceiver());
                e.setIsSign(data.getIsSign());
                e.setIsDifficult(data.getIsDifficult());
                e.setDifficultReason(data.getDifficultReason());
                e.setAmount(data.getAmount());
            }
        }
    }

    public void writeExpressListToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath, false)) { // 将第二个参数设置为 false
            // 写入 CSV 文件的表头
            writer.write("WaybillNumber,WaybillType,SenderAddress,SenderPhone,SenderRemark,SenderTime,ReceiverAddress,ReceiverPhone,ReceiverRemark,ReceiverTime,IsSign,IsDifficult,DifficultReason,Amount\n");

            // 遍历订单信息列表，并写入 CSV 文件
            for (Express express : list) {
                StringBuilder sb = new StringBuilder();
                sb.append(express.getWaybillNumber()).append(",");
                sb.append(express.getWaybillType()).append(",");
                sb.append(express.getSender().getAddress()).append(",");
                sb.append(express.getSender().getPhone()).append(",");
                sb.append(express.getSender().getRemark()).append(",");
                sb.append(express.getSender().getTime().getTime()).append(",");
                sb.append(express.getReceiver().getAddress()).append(",");
                sb.append(express.getReceiver().getPhone()).append(",");
                sb.append(express.getReceiver().getRemark()).append(",");
                sb.append(express.getReceiver().getTime().getTime()).append(",");
                sb.append(express.getIsSign()).append(",");
                sb.append(express.getIsDifficult()).append(",");
                sb.append(express.getDifficultReason()).append(",");
                sb.append(express.getAmount()).append("\n");
                writer.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  List readCSVToExpressList(String filePath) {
        List expressList = new List();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // 跳过 CSV 文件的表头
            reader.readLine();

            // 逐行读取 CSV 文件内容，并转换为 Express 对象添加到链表中
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Express express = new Express();
                express.setWaybillNumber(data[0]);
                express.setWaybillType(data[1]);
                express.getSender().setAddress(data[2]);
                express.getSender().setPhone(data[3]);
                express.getSender().setRemark(data[4]);
                express.getSender().getTime().setTime(parseTime(data[5]));
                express.getReceiver().setAddress(data[6]);
                express.getReceiver().setPhone(data[7]);
                express.getReceiver().setRemark(data[8]);
                express.getReceiver().getTime().setTime(parseTime(data[9]));
                express.setIsSign(Boolean.parseBoolean(data[10]));
                express.setIsDifficult(Boolean.parseBoolean(data[11]));
                express.setDifficultReason(data[12]);
                express.setAmount(Double.parseDouble(data[13]));
                expressList.add(express);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expressList;
    }

    public List sortByTime(Boolean isAscend) {
        List sortedList = new List();

        // 检查原始列表是否为空
        if (list.isEmpty()) {
            return sortedList;
        }

        // 将原始列表中的元素复制到数组中
        Express[] array = list.toArray(new Express[list.size()]);

        // 使用数组进行排序
        Arrays.sort(array, (a, b) -> {
            if (isAscend) {
                return a.getReceiver().getTime().compare(b.getReceiver().getTime());
            } else {
                return b.getReceiver().getTime().compare(a.getReceiver().getTime());
            }
        });

        // 将排序后的数组转换回列表
        for (Express express : array) {
            sortedList.add(express);
        }


        // 返回排序后的列表
        return sortedList;
    }





    private Time parseTime(String time){
        String[] timeArray = time.split("-");
        return new Time(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), Integer.parseInt(timeArray[2]));
    }


    public List subList(int startIndex, int endIndex) {
        List subList = new List();

        // 检查原始列表是否为空
        if (list.isEmpty()) {
            return subList;
        }

        // 检查索引范围是否合法
        if (startIndex < 0 || endIndex >= list.size() || startIndex > endIndex) {
            return subList;
        }

        // 将原始列表中的元素复制到数组中
        Express[] array = list.toArray(new Express[list.size()]);

        // 将数组中的元素添加到子列表中
        for (int i = startIndex; i <= endIndex; i++) {
            subList.add(array[i]);
        }


        // 返回子列表
        return subList;
    }
}
