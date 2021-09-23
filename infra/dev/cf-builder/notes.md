aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 140251415856.dkr.ecr.us-east-2.amazonaws.com
docker build -t cf-builder .
docker tag cf-builder:4 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-builder:4
docker tag cf-builder:latest 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-builder:latest
docker push 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-builder:latest

curl -s "https://get.sdkman.io" | bash

apt-get -y update
apt-get install -y
apt-get install software-properties-common -y

add-apt-repository ppa:linuxuprising/java
apt update
su -
echo "deb http://ppa.launchpad.net/linuxuprising/java/ubuntu focal main" | tee /etc/apt/sources.list.d/linuxuprising-java.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 73C3DB2A
apt-get update
exit
apt install oracle-java16-installer --install-recommends

apt-get install curl -y
apt-get install unzip -y
apt-get install zip -y
curl -s "https://get.sdkman.io" | bash

su -
source "/root/.sdkman/bin/sdkman-init.sh"

sdk install gradle 7.2

curl -fsSL https://deb.nodesource.com/setup_16.x | bash -
apt-get install gcc g++ make -y

curl -sL https://dl.yarnpkg.com/debian/pubkey.gpg | gpg --dearmor | tee /usr/share/keyrings/yarnkey.gpg >/dev/null

echo "deb [signed-by=/usr/share/keyrings/yarnkey.gpg] https://dl.yarnpkg.com/debian stable main" | tee /etc/apt/sources.list.d/yarn.list

apt-get update -y && apt-get install yarn -y

curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"

unzip awscliv2.zip
./aws/install

apt-get update

apt-get install \
 apt-transport-https \
 ca-certificates \
 curl \
 gnupg \
 lsb-release

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

echo \
 "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
 $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

apt-get update
apt-get install docker-ce docker-ce-cli containerd.io -y
