mvn clean install -Dmaven.test.skip=true
sudo docker build -t krypto .
sudo docker stop krypto
sudo docker rm krypto
sudo docker run -d --name krypto -p 3939:8080 krypto