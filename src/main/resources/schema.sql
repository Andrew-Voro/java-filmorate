create TABLE PUBLIC.USERS (
	USER_ID INTEGER NOT NULL AUTO_INCREMENT,
	EMAIL CHARACTER VARYING(50) NOT NULL,
	USER_NAME CHARACTER VARYING(100) NOT NULL,
	LOGIN CHARACTER VARYING(100) NOT NULL,
	BIRTHDAY DATE NOT NULL,
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE PUBLIC.FRIENDS (
                                U_ID INTEGER  NOT NULL,
                                FRIEND_ID INTEGER  NOT NULL,
                                CONSTRAINT FRIENDS_PK PRIMARY KEY (U_ID,FRIEND_ID),
                                CONSTRAINT FRIENDS_FK FOREIGN KEY (U_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE PUBLIC.GENRE (
                                GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
                                GENRE_NAME CHARACTER VARYING(50) NOT NULL,
                                CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
  );

CREATE TABLE PUBLIC.MPA (
                              MPA_ID INTEGER NOT NULL AUTO_INCREMENT,
                              MPA_NAME CHARACTER VARYING(20) NOT NULL,
                              CONSTRAINT MPA_PK PRIMARY KEY (MPA_ID)
);

CREATE TABLE PUBLIC.FILMS (
                                            RELEASE_DATE DATE NOT NULL,
                                            FILM_NAME CHARACTER VARYING(100) NOT NULL,
                                            DURATION INTEGER NOT NULL,
                                            DESCRIPTION CHARACTER VARYING(200) NOT NULL,
                                            MPA INTEGER ,
                                            FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
                                            RATE  INTEGER ,
                                            CONSTRAINT FILMS_FK FOREIGN KEY (MPA) REFERENCES PUBLIC.MPA(MPA_ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                            CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID)
              );

CREATE TABLE PUBLIC.FILM_GENRE (
                              FILM_ID INTEGER NOT NULL ,
                              GENRE_ID INTEGER NOT NULL,
                              CONSTRAINT FILM_GENRE_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
                              CONSTRAINT FILM_GENRE_FK_1 FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(GENRE_ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
                              CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID,GENRE_ID)
);


CREATE TABLE PUBLIC.LIKES (
                              USER_ID INTEGER NOT NULL,
                              FILM_ID INTEGER NOT NULL,
                              CONSTRAINT LIKES_PK PRIMARY KEY (USER_ID,FILM_ID),
                              CONSTRAINT LIKES_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
                              CONSTRAINT LIKES_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID) ON DELETE RESTRICT ON UPDATE RESTRICT

);
