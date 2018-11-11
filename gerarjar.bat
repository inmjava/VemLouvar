git pull
cd exe
git clean -xdf
cd ..
mvn clean install -PgenerateJar
pause