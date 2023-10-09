package mk.ukim.finki.nbafantasy.model;

public interface Subject {
    void register(User o);
    void remove(User o);
    void notifyObservers(Notifications notifications);
}
