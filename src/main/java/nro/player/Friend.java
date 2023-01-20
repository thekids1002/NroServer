package nro.player;

public class Friend {
    public int id;  
    public short head;
    public short headICON;
    public short body;
    public short leg;
    public byte bag;
    public String name;
    public boolean isOnline;
    public String strPower;

    public static class Builder {
        public int id;  
        public short head;
        public short headICON;
        public short body;
        public short leg;
        public byte bag;
        public String name;
        public boolean isOnline;
        public String strPower;

        public Builder(int id) {
            this.id = id;
        }

        public Builder withHead(short head){
            this.head = head;
            return this;  //By returning the builder each time, we can create a fluent interface.
        }

        public Builder withHeadICON(short headICON){
            this.headICON = headICON;
            return this;
        }

        public Builder withBody(short body){
            this.body = body;
            return this;
        }

        public Builder withLeg(short leg){
            this.leg = leg;
            return this;
        }

        public Builder withBag(byte bag){
            this.bag = bag;
            return this;
        }

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder isOnline(boolean isOnline){
            this.isOnline = isOnline;
            return this;
        }

        public Builder withPower(String strPower){
            this.strPower = strPower;
            return this;
        }

        public Friend build(){
            //Here we create the actual bank account object, which is always in a fully initialised state when it's returned.
            Friend friend = new Friend();  //Since the builder is in the BankAccount class, we can invoke its private constructor.
            friend.id = this.id;
            friend.head = this.head;
            friend.headICON = this.headICON;
            friend.body = this.body;
            friend.leg = this.leg;
            friend.bag = this.bag;
            friend.name = this.name;
            friend.isOnline = this.isOnline;
            friend.strPower = this.strPower;

            return friend;
        }
    }
}
