aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 140251415856.dkr.ecr.us-east-2.amazonaws.com

docker build -t cf-server .

docker tag cf-server:latest 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-server:latest

docker push 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-server:latest
