
CREATE  TABLE admin ( 
	mdp varchar(255)    ,
	pseudo varchar(25)    
 );

CREATE  TABLE categorieproduit ( 
	idcategorieproduit integer NOT NULL PRIMARY KEY ,
	intitulecategorieproduit varchar(20) 
 );

CREATE  TABLE utilisateur ( 
	idutilisateur integer NOT NULL PRIMARY KEY  ,
	pseudo varchar(100)  ,
	mdp varchar(255)
 );

CREATE  TABLE enchere ( 
	idenchere integer  NOT NULL PRIMARY KEY   ,
	idenchereur integer  NOT NULL  ,  --id de la personne qui a publie l'enchere
	dateenchere timestamp    ,
	gagnant integer    , --id personne qui a remporter l'enchere doit etre != idenchereur
	sommedepart integer    , -- prix de depart enchere
	datefin timestamp    ,	-- pour avoir la duree de l'enchere
	commission real DEFAULT 0.2   , --pourcentage qui va chez l'admin du site
	prixminimumdevente   integer    , -- meme que sommedepart
	designationproduit   varchar(255)    ,
	idcategorie          integer    ,
	statut               varchar(10) DEFAULT 'en cours'   ,
	FOREIGN KEY ( idenchereur ) REFERENCES utilisateur( idutilisateur )   ,
	FOREIGN KEY ( idcategorie ) REFERENCES categorieproduit( idcategorieproduit )   
 );

CREATE  TABLE mise ( 
	idmise               integer  NOT NULL PRIMARY KEY  ,
	idenchere            integer  NOT NULL  ,
	idmiseur             integer  NOT NULL  ,
	mise                 integer    ,
	datemise             timestamp    ,
	 FOREIGN KEY ( idenchere ) REFERENCES "public".enchere( idenchere )   ,
	 FOREIGN KEY ( idmiseur ) REFERENCES "public".utilisateur( idutilisateur )   
 );



CREATE  TABLE soldeutilisateur ( 
	idsoldeutilisateur   integer NOT NULL PRIMARY KEY  ,
	valeur               integer    ,
	dateoperation        timestamp    ,
	idutilisateur        integer  NOT NULL,
	 FOREIGN KEY ( idutilisateur ) REFERENCES utilisateur( idutilisateur )   
 );


CREATE  TABLE soldeutilisateurvalide ( 
	idsoldeutilisateurvalide integer NOT NULL PRIMARY KEY ,
	idsoldeutilisateur   integer  NOT NULL  ,
	datevalidation       timestamp    ,
	FOREIGN KEY ( idsoldeutilisateur ) REFERENCES soldeutilisateur( idsoldeutilisateur )   
 );


--get latest mise for each idenchere
CREATE VIEW miseMaxDate AS
SELECT *
FROM mise
WHERE datemise = (SELECT MAX(datemise) FROM mise m2 WHERE m2.idenchere = mise.idenchere)
ORDER BY idenchere;

--valeurcommission  | idcategorie
CREATE VIEW commissionCategorie AS
SELECT SUM(enchere.commission*miseMaxDate.mise) as valeurCommission, enchere.idcategorie as idcategorie, categorieproduit.intitulecategorieproduit as nomproduit 
FROM enchere 
JOIN misemaxdate ON miseMaxDate.idenchere = enchere.idenchere 
JOIN categorieproduit ON enchere.idcategorie = categorieproduit.idcategorieproduit 
WHERE enchere.statut = 'clos' 
GROUP BY idcategorie,nomproduit 
ORDER BY valeurCommission DESC;

--get monthly revenu for each enchere
CREATE VIEW monthlyrevenu AS
WITH mise_latest AS (
SELECT idenchere, MAX(datemise) AS latest_datemise
FROM mise
GROUP BY idenchere
)
SELECT
EXTRACT(MONTH FROM date_trunc('month', ml.latest_datemise)) AS month,
SUM(m.mise * e.commission) AS revenu
FROM mise m
JOIN mise_latest ml ON m.idenchere = ml.idenchere AND m.datemise = ml.latest_datemise
JOIN enchere e ON m.idenchere = e.idenchere
GROUP BY month
ORDER BY month;

--get yearly revenu 
CREATE VIEW yearlyrevenu AS
WITH mise_latest AS (
  SELECT idenchere, MAX(datemise) AS latest_datemise
  FROM mise
  GROUP BY idenchere
)
SELECT
  EXTRACT(YEAR FROM date_trunc('year', ml.latest_datemise)) AS year,
  SUM(m.mise * e.commission) AS revenu
FROM mise m
JOIN mise_latest ml ON m.idenchere = ml.idenchere AND m.datemise = ml.latest_datemise
JOIN enchere e ON m.idenchere = e.idenchere
GROUP BY year
ORDER BY year;
