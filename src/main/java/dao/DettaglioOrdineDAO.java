package dao;

import java.sql.SQLException;
import java.util.List;

import model.DettaglioOrdineBean;

public interface DettaglioOrdineDAO {

    public void doSave(DettaglioOrdineBean dettaglio) throws SQLException;

    public List<DettaglioOrdineBean> doRetrieveByOrdine(int idOrdine) throws SQLException;

}
