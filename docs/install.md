---
layout: page
title: Install
permalink: /install/
---

# Installation instructions
### Step 1: Install
### Step 2: Configure nginx
________________________________________________________________
# Install
There are two ways of installing the application.
1. Hosting using docker (Recommended)
2. Hosting on Bare metal

## 1. Hosting using docker

### Install Docker -> Installation instructions [here](https://docs.docker.com/engine/installation/).

### Docker-compose :
Docker compose comes preinstalled with Docker Desktop for Windows and Mac OS.
In linux-based systems(e.g Ubuntu), Docker compose can be installed using the following command:

```
$ sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
$ sudo chmod +x /usr/local/bin/docker-compose
```  
Verify docker and docker-compose installation:

```
$ docker --version
$ docker-compose --version
```
Clone the repository:

```
$ git clone https://github.com/DevGateway/ocportal.git
```
Navigate to the oc-portal directory:

``` 
$ cd ocportal
```
Run the docker compose command:

```
$ docker-compose up --build -d
```

## 2. Installation on Bare metal
### Hosting requirements:
Operating System: GNU Linux/Unix
CPU: 4 cores, 64 bit
RAM Memory: 16 GB
Storage: 350 GB
Network: Public IP address, ports 80 and 443

### Prerequisites:
JDK 17
PostgreSQL 11 with PostGIS 2.4 extension
MongoDB 8.0

### Deliverables:
Main application: ocportal.jar


### Email

Configure SMTP server to serve on localhost port 25 without any authentication.

### PostgreSQL
Database name must be oc-portal. Application connects to the database via 5432 port.

Make sure that postgres user exists and PostgreSQL is configured to trust all local ip v4 connections.

In /etc/postgresql/11/main/pg_hba.conf you must have the following lines:

```
IPv4 local connections:
host	all         	all         	127.0.0.1/32        	trust
```

### MongoDB
Please install MongoDB 8.0 Community Edition based using the instructions [here](https://docs.mongodb.com/v8.0/installation/#mongodb-community-edition-installation-tutorials)

### Application Settings:

| Command                              | Comment |
|--------------------------------------|---------|
| `-DjwtSecret=1234`                   | A secret used to generate JWT tokens. If changed all users of PMC Reporter app will have to login again. |
| `-Dgoogle.recaptcha.secret=4567`     | Google Recaptcha Secret |
| `-DLOG_DIR=/opt/ocportal/logs/`      | Path to the directory where all application logs will be written. |
| `-DgaId=Google-analytics-id`         |  Google Analytics ID | 
| `-DserverURL=https://server.address` | External URL of the service. Used to: generate links that are sent out in emails in OCDS files |
| `-Dfile.encoding=UTF-8`              |Default file encoding. Must be UTF-8. |
| `-DJava.awt.headless=true`           | Instructs JVM to not use display/mouse/keyboard. |
| `-XX:+HeapDumpOnOutOfMemoryError`    | Create heap dump when out of memory error occurs.|
| `-XX:HeapDumpPath=/opt/ocportal/hd`  | Specify the path where to store heap dumps.|
| `-Xms512m`                           | Initial heap size. |
| `-Xmx8192m`                          | Maximum possible heap size. |
| `-XX:+UseG1GC`                       | Use Garbage-First (G1) garbage collector. |
| `-Dwicket.configuration=deployment`  | Must be set to deployment. |
| `-server`                            | Enable JVM server optimizations. |



###  Installation steps:

Create ocportal user.

`$ useradd ocportal`

Create /opt/ocportal, /opt/ocportal/logs and /opt/ocportal/heapdumps folders.


`$ mkdir -p /opt/ocportal/logs /opt/ocportal/heapdumps`

Copy ocportal.jar to /opt/ocportal folder.

`$ cp ocportal.jar /opt/ocportal`

Make sure that /opt/ocportal and its content is owned by ocportal user.

`$ chown -R ocportal /opt/ocportal`

Register, configure and enable the app as a system service.

Use ocportal.service from [Annex 1](#annex-1) and copy it to /etc/systemd/system directory.

```
$ systemctl enable ocportal
$ systemctl start ocportal
```


## Nginx Configuration

Web requests to the applications are dispatched by Nginx 1.14 webserver, running on Debian GNU/Linux Stable (Buster) AMD64.

The webserver is configured to redirect any plain text (HTTP) requests to their TLS-encrypted (HTTPS) counterparts. Additionally, it is enforcing Strict Transport Security by pinning the necessary headers. TLS certificate is provided by LetsEncrypt service. The server is configured to only allow secure modern encryption protocols (TLS v1.2 and v1.3) as well as only strong cipher suites.

The server requests compliant robots not to index the website by serving a restrictive robots.txt file.

Configuration file:

```
events {
}

http {
include /etc/nginx/mime.types;
client_max_body_size 32m;
gzip on;
gzip_types application/javascript application/json application/x-javascript application/xml application/xml+rss image/svg+xml text/css text/javascript text/xml;
gzip_vary on;
proxy_http_version 1.1;
ssl_ciphers 'HIGH !CAMELLIA !PSK !kRSA !SHA1 !SHA256 !SHA384';
ssl_dhparam /etc/ssl/dhparam;
ssl_prefer_server_ciphers on;
ssl_protocols TLSv1.2 TLSv1.3;
ssl_session_cache shared:SSL:10m;
ssl_session_timeout 5m;
server {
add_header Strict-Transport-Security 'max-age=86400; includeSubDomains' always;
client_max_body_size 32m;
gzip on;
gzip_types application/javascript application/json application/x-javascript application/xml application/xml+rss image/svg+xml text/css text/javascript text/xml;
gzip_vary on;
listen 443 ssl http2;
listen [::]:443 ssl http2;
proxy_http_version 1.1;
proxy_set_header Host $http_host;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
proxy_set_header X-Forwarded-Proto $scheme;
proxy_set_header X-Real-IP $remote_addr;
server_name server.address;
ssl_certificate /etc/letsencrypt/live/server.address/fullchain.pem;
ssl_certificate_key /etc/letsencrypt/live/server.address/privkey.pem;

	location / {
    	proxy_pass http://ocportal;
	}

	location /ci {
    	proxy_pass http://ocportal:8082;
	}

	location = /robots.txt {
    	expires 7d;
    	return 200 'User-Agent: *\nDisallow: /\n';
	}
}
}
```

### Annex 1

Example ocportal.service file:

```

[Unit]
Description=OCPortal
After=syslog.target postgresql.service
BindsTo=postgresql.service

[Service]
User=ocportal
Group=ocportal
WorkingDirectory=/opt/ocportal
ExecStart=/usr/bin/java -jar -server -Dwicket.configuration=deployment -Dsmsgateway.key=1 -DjwtSecret=1234 -Dgoogle.recaptcha.secret=4567 -Dfile.encoding=UTF-8 -Xms512m -Xmx8192m -XX:MaxMetaspaceSize=256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/ocportal/hd
-XX:ReservedCodeCacheSize=128m -DJava.awt.headless=true -XX:+UseG1GC
-DLOG_DIR=/opt/ocportal/logs/ -Dbackup.home=/opt/backup -DgaId=google-analytics-id -DserverURL=https://server.address -jar /opt/ocportal/ocportal.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```
