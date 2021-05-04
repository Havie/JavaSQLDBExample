CREATE TABLE Tournament (
accessCode varchar(10) PRIMARY KEY,
signupMode integer,
bracketMode bool,
tName varchar(60),
startDate date,
endDate date,
minTeamSize integer,
maxTeamSize integer,
idealTeamSize integer);

CREATE TABLE Team (
teamId integer PRIMARY KEY,
teamName varchar(60),
tournamentId varchar(10),
FOREIGN KEY(tournamentId) REFERENCES Tournament(accessCode));

CREATE TABLE Participant (
participantId integer PRIMARY KEY,
firstName varchar(20),
lastName varchar(20),
organization varchar(80),
teamId integer,
FOREIGN KEY(teamId) REFERENCES Team(teamId));

CREATE TABLE BracketMatch (
matchId integer PRIMARY KEY,
round integer,
isWinners bool,
played bool,
team1Id integer,
team2Id integer,
winnerId integer,
score varchar(5),
FOREIGN KEY(team1Id) REFERENCES Team(teamId),
FOREIGN KEY(team2Id) REFERENCES Team(teamId),
FOREIGN KEY(winnerId) REFERENCES Team(teamId));

INSERT INTO Tournament VALUES ('0001', 0, false, 'Test Tourney', DATE '2021-04-28', DATE '2021-04-29', 2, 4, 3);
INSERT INTO Team VALUES (1, 'Group 1', '0001'), (2, 'Group 2', '0001'), (3, 'Group 3', '0001'), (4, 'Group 4', '0001');
INSERT INTO Participant VALUES (1, 'Joe', 'Smith', 'IMDB', 1), (2, 'Maria', 'Smith', 'IMDB', 1);
INSERT INTO Participant VALUES (3, 'Steve', 'Smith', 'GLHF', 2), (4, 'Olivia', 'Smith', 'GLHF', 2);
INSERT INTO Participant VALUES (5, 'Tyler', 'Smith', 'DBS', 3), (6, 'Victoria', 'Smith', 'DBS', 3);
INSERT INTO Participant VALUES (7, 'Rachel', 'Smith', 'KLR', 4), (8, 'Alex', 'Smith', 'KLR', 4);
INSERT INTO BracketMatch VALUES (1, 1, true, false, 1, 2, null, '00-00'), (2, 1, true, false, 3, 4, null, '00-00');