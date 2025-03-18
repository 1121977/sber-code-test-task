package ru.securecodereview.core.model;

public class Client {
    private final long id;
    private final String name;

    public Client(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Client)){
            return false;
        } else {
            return this.id == ((Client) object).getId() && this.name.equals(((Client)object).getName());
        }
    }

    @Override
    public int hashCode(){
        int result = 220;
        int c = this.name.hashCode();
        result = 37 * result + c;
        c = Long.hashCode(id);
        return 37 * result + c;
    }
}
