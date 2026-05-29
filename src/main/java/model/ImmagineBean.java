package model;

import java.io.Serializable;
import java.util.Objects;

public class ImmagineBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private int idImmagine;
    private String path;

    public ImmagineBean() {
    }

	public int getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(int idImmagine) {
		this.idImmagine = idImmagine;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmagineBean other = (ImmagineBean) obj;
		return idImmagine == other.idImmagine && Objects.equals(path, other.path);
	}   
}
