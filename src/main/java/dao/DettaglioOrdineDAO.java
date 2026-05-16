package dao;

import java.sql.SQLException;
import java.util.Collection;

import model.DettaglioOrdineBean;
import model.ProdottoBean;

public interface DettaglioOrdineDAO {

    public void doSave(DettaglioOrdineBean dettaglio) throws SQLException;

    public Collection<ProdottoBean> doRetrieveByOrdine(int idOrdine) throws SQLException;

}
