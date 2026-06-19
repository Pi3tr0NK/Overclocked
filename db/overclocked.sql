DROP DATABASE if exists overclocked;
CREATE DATABASE overclocked;
USE overclocked;

CREATE TABLE indirizzo(
	id_indirizzo int PRIMARY KEY AUTO_INCREMENT,
    via_numciv varchar(255),
    paese varchar(100),
    citta varchar(100),
    provincia varchar(100),
	dati_plus varchar(255),
    codice_postale varchar(10)
);

CREATE TABLE utente(
	id_utente int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) NOT NULL UNIQUE,
    nome varchar(20) NOT NULL,
    cognome varchar(20) NOT NULL,
    password varchar(255) NOT NULL,
    ruolo enum('USER','ADMIN') NOT NULL DEFAULT('USER'),
    cellulare varchar(50),
    fk_indirizzo int NOT NULL,
    
    FOREIGN KEY(fk_indirizzo) REFERENCES indirizzo(id_indirizzo) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE ordine(
	id_ordine int PRIMARY KEY AUTO_INCREMENT,
    data date NOT NULL,
    stato enum('IN_PREPARAZIONE','SPEDITO','CONSEGNATO','RIMBORSATO') DEFAULT 'IN_PREPARAZIONE',
    totale decimal (10,2) NOT NULL,
    pagamento varchar(4) NOT NULL,
	fk_utente int NOT NULL,
	fk_indirizzo int NOT NULL,
    
	
    FOREIGN KEY(fk_utente) REFERENCES utente(id_utente) ON UPDATE cascade ON DELETE cascade,
    FOREIGN KEY(fk_indirizzo) REFERENCES indirizzo(id_indirizzo) ON UPDATE cascade ON DELETE restrict
);

CREATE TABLE prodotto(
	id_prodotto int PRIMARY KEY AUTO_INCREMENT,
	nome varchar(255) NOT NULL,
	modello varchar(255) NOT NULL,
    descrizione varchar(255),
    marca varchar(255) NOT NULL,
    prezzo decimal(10,2) NOT NULL,
    stock int DEFAULT 0 NOT NULL,
    dimensioni varchar(50) NOT NULL,		
    peso varchar(50) NOT NULL,
    attivo boolean NOT NULL,
    sconto INT NOT NULL DEFAULT 0 CHECK (sconto >= 0 AND sconto <= 100) NOT NULL,
    categoria varchar(50) NOT NULL
);

CREATE TABLE immagini(
	id_immagine int PRIMARY KEY AUTO_INCREMENT,
	path varchar(255),
    fk_prodotto int,
    
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE dettagliOrdine(
	fk_ordine int,
   	fk_prodotto int ,
    quantita int DEFAULT 1,
    prezzo_unitario decimal(10,2),
   	
   	PRIMARY KEY(fk_ordine,fk_prodotto),
   	
    FOREIGN KEY(fk_ordine) REFERENCES ordine(id_ordine) ON UPDATE cascade ON DELETE cascade,
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE restrict
);

CREATE TABLE psu(
	id_psu int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	potenza int NOT NULL,
	certificazione varchar(255) NOT NULL,
	modulare enum ('MODULARE','SEMIMODULARE','NON_MODULARE') NOT NULL,
	formato enum ('ATX','SFX') NOT NULL,
	
	PRIMARY KEY(id_psu,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE dissipatore(
	id_dissipatore int AUTO_INCREMENT,
    fk_prodotto int NOT NULL,
	tipo enum('ARIA', 'LIQUIDO') NOT NULL,
	socket_supportati varchar(255) NOT NULL,
	dimensioni_ventola varchar(20) NOT NULL,
	rpm_max int NOT NULL,
	rumore int NOT NULL,
	tdp_supportato int NOT NULL,
	
	PRIMARY KEY(id_dissipatore,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE cpu(
	id_cpu int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	core int NOT NULL,
	thread int NOT NULL,
	frequenza varchar(20) NOT NULL,
    frequenza_ram varchar(20) NOT NULL,
    tiporam varchar(10) NOT NULL,
	socket varchar(20) NOT NULL,
	tdp int NOT NULL,
	
    PRIMARY KEY(id_cpu,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE ram(
	id_ram int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	capacita varchar(10) NOT NULL,
	frequenza varchar(20) NOT NULL,
	tipo varchar(10) NOT NULL,
	
	PRIMARY KEY(id_ram,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE chassis(
	id_case int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	formato varchar(20) NOT NULL,		
    colore varchar(20) NOT NULL,
    materiale varchar(255) NOT NULL,
	
    PRIMARY KEY(id_case,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE gpu(
	id_gpu int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	frequenza varchar(20) NOT NULL,
	vram varchar(10) NOT NULL,
	video varchar(50) NOT NULL,
	tipovram varchar(20) NOT NULL,
	pcie varchar(10) NOT NULL,
	maxres varchar(20) NOT NULL,
	tdp int NOT NULL,

    PRIMARY KEY(id_gpu,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE mobo(
	id_mobo int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	
	chipset varchar(20) NOT NULL,
	socket varchar(20) NOT NULL,
	tiporam varchar(10) NOT NULL,
	maxfreq varchar(20) NOT NULL,
	formato varchar(20) NOT NULL,	
	pcie varchar(10) NOT NULL,
	slotram int NOT NULL,
	nvme boolean NOT NULL,
	portesata int NOT NULL,
	porteusb int NOT NULL,
	
    PRIMARY KEY(id_mobo,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);

CREATE TABLE memoria(
	id_memoria int AUTO_INCREMENT,
	fk_prodotto int NOT NULL,
	
	capacita varchar(10) NOT NULL,
	vel_scrittura int NOT NULL,
	vel_lettura int NOT NULL,
	tipo enum ('SSD','HDD') NOT NULL,
	tecnologia enum ('SATA','NVME') NOT NULL,
	formato varchar(20) NOT NULL,
	
	PRIMARY KEY(id_memoria,fk_prodotto),
    FOREIGN KEY(fk_prodotto) REFERENCES prodotto(id_prodotto) ON UPDATE cascade ON DELETE cascade
);