-- PSU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('Alimentatore 750W', 'RM750e', 'Alimentatore modulare 80 Plus Gold', 'Corsair', 129.99, 15, '150x86x160mm', '1.8kg', true, 10, 'PSU'),
('Alimentatore 850W', 'RM850x', 'Alimentatore modulare 80 Plus Platinum', 'Corsair', 169.99, 10, '150x86x160mm', '1.9kg', true, 5, 'PSU'),
('Alimentatore 650W', 'SuperNOVA 650 G6', 'Alimentatore semi-modulare 80 Plus Gold', 'EVGA', 99.99, 20, '150x86x140mm', '1.6kg', true, 0, 'PSU');

SET @first_id = LAST_INSERT_ID();
INSERT INTO psu(fk_prodotto, potenza, certificazione, modulare, formato)
VALUES
(@first_id,     750, '80 Plus Gold',     'MODULARE',     'ATX'),
(@first_id + 1, 850, '80 Plus Platinum', 'MODULARE',     'ATX'),
(@first_id + 2, 650, '80 Plus Gold',     'SEMIMODULARE', 'ATX');

-- Dissipatore
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('Dissipatore ad aria',   'AK400',    'Dissipatore tower silenzioso',    'DeepCool', 39.99,  20, '155mm',        '0.7kg', true, 0,  'DISSIPATORE'),
('Dissipatore AIO 240mm', 'Kraken X53','Dissipatore a liquido 240mm',    'NZXT',    109.99,  12, '240x120x27mm', '1.2kg', true, 10, 'DISSIPATORE'),
('Dissipatore ad aria',   'NH-D15',   'Dissipatore dual tower premium',  'Noctua',   99.99,   8, '165mm',        '1.3kg', true, 0,  'DISSIPATORE');

SET @first_id = LAST_INSERT_ID();
INSERT INTO dissipatore(fk_prodotto, tipo, socket_supportati, dimensioni_ventola, rpm_max, rumore, tdp_supportato)
VALUES
(@first_id,     'ARIA',   'AM4,AM5,LGA1700', '120mm', 1850, 29, 220),
(@first_id + 1, 'LIQUIDO','AM4,AM5,LGA1700', '120mm', 2800, 35, 300),
(@first_id + 2, 'ARIA',   'AM4,AM5,LGA1700', '140mm', 1500, 25, 250);

-- CPU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('Processore Ryzen 7',      '7800X3D', 'CPU gaming ad alte prestazioni',  'AMD',   399.99,  8, NULL, NULL, true, 5, 'CPU'),
('Processore Intel Core i5','14600K',  'CPU Intel di nuova generazione',  'Intel', 329.99,  7, NULL, NULL, true, 0, 'CPU'),
('Processore Ryzen 5',      '7600X',   'CPU AMD entry-level AM5',         'AMD',   249.99, 15, NULL, NULL, true, 8, 'CPU');

SET @first_id = LAST_INSERT_ID();
INSERT INTO cpu(fk_prodotto, core, thread, frequenza, frequenza_ram, tiporam, socket, tdp)
VALUES
(@first_id,      8, 16, '4.2GHz', 'DDR5-5200', 'DDR5', 'AM5',     120),
(@first_id + 1, 14, 20, '5.3GHz', 'DDR5-5600', 'DDR5', 'LGA1700', 125),
(@first_id + 2,  6, 12, '4.7GHz', 'DDR5-5200', 'DDR5', 'AM5',     105);

-- RAM
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('RAM 32GB DDR5', 'Vengeance',  'Kit RAM DDR5 ad alta velocità', 'Corsair',  149.99, 30, NULL, NULL, true, 15, 'RAM'),
('RAM 16GB DDR5', 'Trident Z5', 'Kit RAM DDR5 gaming',           'G.Skill',   89.99, 25, NULL, NULL, true,  5, 'RAM'),
('RAM 64GB DDR5', 'Fury Beast', 'Kit RAM DDR5 per workstation',  'Kingston', 229.99, 10, NULL, NULL, true,  0, 'RAM');

SET @first_id = LAST_INSERT_ID();
INSERT INTO ram(fk_prodotto, capacita, frequenza, tipo)
VALUES
(@first_id,     '32GB', '6000MHz', 'DDR5'),
(@first_id + 1, '16GB', '6400MHz', 'DDR5'),
(@first_id + 2, '64GB', '5600MHz', 'DDR5');

-- CASE
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('Case Mid Tower',  'H5 Flow',      'Case con airflow ottimizzato',      'NZXT',           109.99, 10, '465x227x446mm', '7.3kg',  true, 0,  'CASE'),
('Case Full Tower', 'Meshify 2 XL', 'Case full tower con pannello mesh', 'Fractal Design', 179.99,  6, '543x240x497mm', '11.5kg', true, 5,  'CASE'),
('Case Mini ITX',   'A4-H2O',       'Case compatto per build SFF',       'Lian Li',         89.99,  8, '325x155x292mm', '3.8kg',  true, 10, 'CASE');

SET @first_id = LAST_INSERT_ID();
INSERT INTO chassis(fk_prodotto, formato, colore, materiale)
VALUES
(@first_id,     'ATX',   'Nero',    'Acciaio e vetro temperato'),
(@first_id + 1, 'E-ATX', 'Nero',    'Acciaio e pannello mesh'),
(@first_id + 2, 'ITX',   'Argento', 'Alluminio');

-- GPU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('Scheda Video RTX 4070',    'RTX 4070 Dual',       'GPU NVIDIA di fascia alta',  'ASUS',     649.99, 5, '267mm', '1.1kg', true, 5, 'GPU'),
('Scheda Video RX 7800 XT',  'Pulse RX 7800 XT',    'GPU AMD per gaming 2K/4K',   'Sapphire', 579.99, 6, '320mm', '1.4kg', true, 8, 'GPU'),
('Scheda Video RTX 4060 Ti', 'RTX 4060 Ti Gaming X','GPU NVIDIA mid-range',        'MSI',      449.99, 9, '280mm', '1.0kg', true, 0, 'GPU');

SET @first_id = LAST_INSERT_ID();
INSERT INTO gpu(fk_prodotto, frequenza, vram, video, tipovram, pcie, maxres, tdp)
VALUES
(@first_id,     '2475MHz', '12GB', 'HDMI, DisplayPort', 'GDDR6X', 'PCIe 4.0', '7680x4320', 200),
(@first_id + 1, '2430MHz', '16GB', 'HDMI, DisplayPort', 'GDDR6',  'PCIe 4.0', '7680x4320', 263),
(@first_id + 2, '2535MHz', '8GB',  'HDMI, DisplayPort', 'GDDR6',  'PCIe 4.0', '7680x4320', 165);

-- MOBO
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('Scheda Madre B650', 'B650 Gaming X AX', 'Scheda madre AM5 ATX',              'Gigabyte', 219.99, 12, 'ATX',       NULL, true, 0, 'MOBO'),
('Scheda Madre Z790', 'Z790 Apex',        'Scheda madre LGA1700 per overclock', 'ASUS',     399.99,  5, 'ATX',       NULL, true, 0, 'MOBO'),
('Scheda Madre B760', 'B760M Pro',        'Scheda madre LGA1700 Micro-ATX',     'MSI',      159.99, 15, 'Micro-ATX', NULL, true, 5, 'MOBO');

SET @first_id = LAST_INSERT_ID();
INSERT INTO mobo(fk_prodotto, chipset, socket, tiporam, maxfreq, formato, pcie, slotram, nvme, portesata, porteusb)
VALUES
(@first_id,     'B650', 'AM5',     'DDR5', '6400MHz', 'ATX',       'PCIe 4.0', 4, true, 4, 8),
(@first_id + 1, 'Z790', 'LGA1700', 'DDR5', '7200MHz', 'ATX',       'PCIe 5.0', 4, true, 6, 10),
(@first_id + 2, 'B760', 'LGA1700', 'DDR5', '5600MHz', 'Micro-ATX', 'PCIe 4.0', 2, true, 4, 6);

-- STORAGE
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto, categoria)
VALUES
('SSD NVMe 1TB',   '980 Pro',        'SSD ad alte prestazioni PCIe 4.0', 'Samsung',        119.99, 25, '2280', NULL,    true, 10, 'STORAGE'),
('SSD NVMe 2TB',   'WD Black SN850X','SSD gaming ad alte prestazioni',   'Western Digital',189.99, 18, '2280', NULL,    true,  5, 'STORAGE'),
('SSD SATA 500GB', '870 EVO',        'SSD SATA per storage secondario',  'Samsung',         69.99, 30, '2.5"', '0.05kg',true,  0, 'STORAGE');

SET @first_id = LAST_INSERT_ID();
INSERT INTO memoria(fk_prodotto, capacita, vel_scrittura, vel_lettura, tipo, tecnologia, formato)
VALUES
(@first_id,     '1TB',   5000, 7000, 'SSD', 'NVME', 'M.2'),
(@first_id + 1, '2TB',   6600, 7300, 'SSD', 'NVME', 'M.2'),
(@first_id + 2, '500GB',  530,  560, 'SSD', 'SATA', '2.5"');

-- UTENTE: user@a.b
INSERT INTO indirizzo (via_numciv, paese, citta, provincia, dati_plus, codice_postale)
VALUES ('Via Roma 10', 'Italia', 'Pescara', 'PE', NULL, '65100');

INSERT INTO utente (email, nome, cognome, password, ruolo, cellulare, fk_indirizzo)
VALUES ('user@a.b',
    'Mario',
    'Rossi',
    'b14361404c078ffd549c03db443c3fede2f3e534d73f78f77301ed97d4a436a9fd9db05ee8b325c0ad36438b43fec8510c204fc1c1edb21d0941c00e9e2c1ce2',
    'USER',
    '3331234567',
    LAST_INSERT_ID()
);

-- UTENTE: admin@a.b
INSERT INTO indirizzo (via_numciv, paese, citta, provincia, dati_plus, codice_postale)
VALUES ('Via Milano 25', 'Italia', 'Milano', 'MI', NULL, '20100');

INSERT INTO utente (email, nome, cognome, password, ruolo, cellulare, fk_indirizzo)
VALUES (
    'admin@a.b',
    'Luca',
    'Bianchi',
    'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec',
    'ADMIN',
    '3399876543',
    LAST_INSERT_ID()
);

-- =========================================================
-- ORDINI: 3 ordini per user@a.b
-- =========================================================
INSERT INTO ordine (data, stato, totale, pagamento, fk_utente, fk_indirizzo)
SELECT '2026-05-02', 'CONSEGNATO', 0, 'VISA', u.id_utente, u.fk_indirizzo
FROM utente u WHERE u.email = 'user@a.b';

SET @ord1 = LAST_INSERT_ID();

INSERT INTO ordine (data, stato, totale, pagamento, fk_utente, fk_indirizzo)
SELECT '2026-05-20', 'SPEDITO', 0, 'MAST', u.id_utente, u.fk_indirizzo
FROM utente u WHERE u.email = 'user@a.b';

SET @ord2 = LAST_INSERT_ID();

INSERT INTO ordine (data, stato, totale, pagamento, fk_utente, fk_indirizzo)
SELECT '2026-06-10', 'IN_PREPARAZIONE', 0, 'PAYP', u.id_utente, u.fk_indirizzo
FROM utente u WHERE u.email = 'user@a.b';

SET @ord3 = LAST_INSERT_ID();

-- Dettagli ordine 1 (user@a.b): CPU Ryzen 7 7800X3D + RAM 32GB DDR5
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord1, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = '7800X3D';
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord1, p.id_prodotto, 2, p.prezzo FROM prodotto p WHERE p.modello = 'Vengeance';

-- Dettagli ordine 2 (user@a.b): Case Mid Tower H5 Flow + SSD NVMe 1TB
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord2, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'H5 Flow';
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord2, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = '980 Pro';

-- Dettagli ordine 3 (user@a.b): GPU RTX 4070 Dual
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord3, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'RTX 4070 Dual';

-- Aggiorno il totale dei 3 ordini calcolandolo dai dettagli
SET SQL_SAFE_UPDATES = 0;

UPDATE ordine o
JOIN (
    SELECT fk_ordine, SUM(quantita * prezzo_unitario) AS tot
    FROM dettagliOrdine
    WHERE fk_ordine IN (@ord1, @ord2, @ord3)
    GROUP BY fk_ordine
) d ON d.fk_ordine = o.id_ordine
SET o.totale = d.tot;

-- =========================================================
-- ORDINI: 3 ordini per admin@a.b
-- =========================================================
INSERT INTO ordine (data, stato, totale, pagamento, fk_utente, fk_indirizzo)
SELECT '2026-04-15', 'CONSEGNATO', 0, 'VISA', u.id_utente, u.fk_indirizzo
FROM utente u WHERE u.email = 'admin@a.b';

SET @ord4 = LAST_INSERT_ID();

INSERT INTO ordine (data, stato, totale, pagamento, fk_utente, fk_indirizzo)
SELECT '2026-05-28', 'RIMBORSATO', 0, 'MAST', u.id_utente, u.fk_indirizzo
FROM utente u WHERE u.email = 'admin@a.b';

SET @ord5 = LAST_INSERT_ID();

INSERT INTO ordine (data, stato, totale, pagamento, fk_utente, fk_indirizzo)
SELECT '2026-06-15', 'IN_PREPARAZIONE', 0, 'PAYP', u.id_utente, u.fk_indirizzo
FROM utente u WHERE u.email = 'admin@a.b';

SET @ord6 = LAST_INSERT_ID();

-- Dettagli ordine 4 (admin@a.b): Mobo Z790 Apex + Dissipatore AIO 240mm
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord4, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'Z790 Apex';
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord4, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'Kraken X53';

-- Dettagli ordine 5 (admin@a.b): Alimentatore 850W RM850x
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord5, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'RM850x';

-- Dettagli ordine 6 (admin@a.b): RAM 64GB DDR5 + SSD NVMe 2TB
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord6, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'Fury Beast';
INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario)
SELECT @ord6, p.id_prodotto, 1, p.prezzo FROM prodotto p WHERE p.modello = 'WD Black SN850X';

-- Aggiorno il totale dei 3 ordini calcolandolo dai dettagli
UPDATE ordine o
JOIN (
    SELECT fk_ordine, SUM(quantita * prezzo_unitario) AS tot
    FROM dettagliOrdine
    WHERE fk_ordine IN (@ord4, @ord5, @ord6)
    GROUP BY fk_ordine
) d ON d.fk_ordine = o.id_ordine
SET o.totale = d.tot;

SET SQL_SAFE_UPDATES = 1;