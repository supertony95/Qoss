/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mobility;

/**
 *
 * @author afsantamaria
 */
public class car_node {
    double x;
    double y;
    int id;
    String destinazione;

    public String getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}

	public car_node(double x, double y, int id,String destinazione) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.destinazione=destinazione;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
   
    
}
