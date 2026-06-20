package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarrelloBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CarrelloItemBean> items;

    public CarrelloBean() {
        items = new ArrayList<>();
    }

    public List<CarrelloItemBean> getItems() {
        return items;
    }

    public void addProduct(ProdottoBean prodotto, int quantita) {

        for (CarrelloItemBean item : items) {
            if (item.getProdotto().getIdProdotto() == prodotto.getIdProdotto()) {
                item.aumentaQuantita(quantita);
                return;
            }
        }

        items.add(new CarrelloItemBean(prodotto, quantita));
    }

    public void removeProduct(int idProdotto) {

        Iterator<CarrelloItemBean> it = items.iterator();

        while (it.hasNext()) {
            CarrelloItemBean item = it.next();

            if (item.getProdotto().getIdProdotto() == idProdotto) 
            {
                it.remove();
                return;
            }
        }
    }
    
    public CarrelloItemBean findProduct(int idProdotto)
    {
    	Iterator<CarrelloItemBean> it = items.iterator();

        while (it.hasNext()) {
            CarrelloItemBean item = it.next();

            if (item.getProdotto().getIdProdotto() == idProdotto) 
                return item;
        }
        return null;
    }

    public int getTotalQuantity() {

        int total = 0;

        for (CarrelloItemBean item : items) {
            total += item.getQuantita();
        }

        return total;
    }

    public void clear() {
        items.clear();
    }
}