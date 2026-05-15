package dao;

import java.util.Collection;

import model.ImmagineBean;

public interface ImmaginiDAO {
	public void doSave(ImmagineBean immagine);

	public boolean doDelete(int code);

	public Collection<ImmagineBean> doRetrieveByProdotto(int idProdotto);
	
	public void updateImage(int idImmagine, String path);
}

