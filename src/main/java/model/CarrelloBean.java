package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarrelloBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProdottoBean> prodotti;

	public CarrelloBean() {
		prodotti = new ArrayList<ProdottoBean>();
	}

	public void addProduct(ProdottoBean product) {
		prodotti.add(product);
	}

	public void deleteProduct(ProdottoBean product) {
		for (ProdottoBean prod : prodotti) {
			if (prod.getIdProdotto() == product.getIdProdotto()) {
				prodotti.remove(prod);
				break;
			}
		}
	}

	public List<ProdottoBean> getProducts() {
		return prodotti;
	}
}
