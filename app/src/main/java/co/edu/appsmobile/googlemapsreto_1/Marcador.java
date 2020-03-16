package co.edu.appsmobile.googlemapsreto_1;

import java.io.Serializable;

public class Marcador implements Serializable {

    private String title;
    private String adress;

    public Marcador(String adress) {
        this.adress = adress;
    }

    public String getDireccion() {
        return adress;
    }

    public void setDireccion(String adress) {
        this.adress = adress;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
