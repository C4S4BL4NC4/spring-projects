// Runs once, on the first startup of an empty mongo-data volume.
// Creates the app user from DB_USER / DB_PASSWORD in docker-compose.yml.
db = db.getSiblingDB(process.env.MONGO_INITDB_DATABASE);

db.createUser({
  user: process.env.DB_USER,
  pwd: process.env.DB_PASSWORD,
  roles: [{ role: "readWrite", db: process.env.MONGO_INITDB_DATABASE }],
});
