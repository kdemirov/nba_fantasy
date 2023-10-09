package mk.ukim.finki.nbafantasy.model;

import lombok.Data;


import javax.persistence.*;
import java.util.List;
@Data
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer number;
    private String position;
    private String height;
    private String weightInLbs;
    private String birthDate;
    private Integer age;
    private String expirience;
    private String school;
    private String playerUrl;
    private String playerImageUrl;
    private Integer fantasyPointPerGame;
    private Integer fantasyPointsWeekly;
    private Integer totalFantasyPoints;
    private double price;
    @ManyToOne
    private Team team;
    public Player(){}
    public Player(String name, Integer number, String position, String height, String weightInLbs, String birthDate, Integer age, String expirience, String school) {
        this.name = name;
        this.number = number;
        this.position = position;
        this.height = height;
        this.weightInLbs = weightInLbs;
        this.birthDate = birthDate;
        this.age = age;
        this.expirience = expirience;
        this.school = school;
        this.playerUrl=null;
        this.playerImageUrl=null;
        this.fantasyPointPerGame=0;
        this.fantasyPointsWeekly=0;
        this.totalFantasyPoints=0;
        this.price =0;
    }

    public void setFantasyPointsWeekly(Integer fantasyPointsWeekly) {
        this.fantasyPointsWeekly += fantasyPointsWeekly;
    }
    public void resetWeeklyPoints(){
        this.fantasyPointsWeekly=0;
    }
    public void resetPointsPerGame(){
        this.fantasyPointPerGame=0;
    }
    public void setTotalFantasyPoints(Integer points) {
        this.totalFantasyPoints +=points;
    }
    public void calculateFantasyPointsPerGame(Integer personalFouls,Integer points,Integer minutesPlayed){


        Double avgPoints=(double)points/10;

        Double minusPoints=(double)personalFouls*0.1;

        if(minutesPlayed>24){
            avgPoints+=1;
        }
        Double finalPoints=avgPoints-minusPoints;

        if(finalPoints<0){
            this.fantasyPointPerGame=0;
        }else{
            this.fantasyPointPerGame= finalPoints.intValue();
            double rest=finalPoints-finalPoints.intValue();
            if(rest>=0.5){
                this.fantasyPointPerGame+=1;
            }

        }

        setFantasyPointsWeekly(this.fantasyPointPerGame);
        setTotalFantasyPoints(this.fantasyPointPerGame);
    }
    public static Player factoryPlayer(List<String> tmp){
        String name=tmp.get(0);
        Integer number=Integer.parseInt(tmp.get(1));
        String position=tmp.get(2);
        String height=tmp.get(3);
        String weightInLbs=tmp.get(4);
        String birthDate=tmp.get(5);
        Integer age=Integer.parseInt(tmp.get(6));
        String expirience=tmp.get(7);
        String school=tmp.get(8);
        return new Player(name,number,position,height,weightInLbs,birthDate,age,expirience,school);
    }


}
