terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# Configure the AWS Provider
provider "aws" {
  region = "us-east-1"
}

#1. Create vpc (virtual private cloud)
resource "aws_vpc" "prod-vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "production"
  }
}

#2. Create internet gateway
resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.prod-vpc.id
}

#3. Create custom route table so traffic from our subnet can go out to the internet
resource "aws_route_table" "prod-route-table" {
  vpc_id = aws_vpc.prod-vpc.id

  route {
    # default gateway. sends all ipv4 traffic to our default gateway so it can get out to the internet
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  route {
    # default gateway. sends all ipv6 traffic to our default gateway so it can get out to the internet
    ipv6_cidr_block = "::/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "prod"
  }
}

#4. Create subnet
resource "aws_subnet" "subnet-1" {
  vpc_id     = aws_vpc.prod-vpc.id
  #  needs to fall within range
  cidr_block = "10.0.1.0/24"
  #optional
  availability_zone = "us-east-1a"

  tags = {
    Name = "prod-subnet"
  }
}

#5. Associate subnet with route table
resource "aws_route_table_association" "a" {
  subnet_id = aws_subnet.subnet-1.id
  route_table_id = aws_route_table.prod-route-table.id
}

#6. Create security group to allow port 22, 80, 443
resource "aws_security_group" "allow_web" {
  name        = "allow_web"
  description = "Allow Web inbound traffic"
  vpc_id      = aws_vpc.prod-vpc.id

  ingress {
    description      = "HTTP"
    from_port        = 80
    to_port          = 80
    protocol         = "tcp"
    # who is allowed to use it
    cidr_blocks      = ["0.0.0.0/0"]
  }
  ingress {
    description      = "SSH"
    from_port        = 22
    to_port          = 22
    protocol         = "tcp"
    # who is allowed to use it
    cidr_blocks      = ["0.0.0.0/0"]
    #    ipv6_cidr_blocks = ["::/0"]
  }

egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    #    ipv6_cidr_blocks = ["::/0"]
  }

  tags = {
    Name = "allow_web"
  }
}

#7. Create a network interface with an ip in the subnet created in step 4
resource "aws_network_interface" "web-server-nic" {
  subnet_id       = aws_subnet.subnet-1.id
  # private ip address for the host
  private_ips     = ["10.0.1.50"]
  security_groups = [aws_security_group.allow_web.id]
}

#8. Assign elastic ip to network interface created in step 7.
# dependent on internet gateway
# public ip address for web traffic
resource "aws_eip" "one" {
#  vpc                       = true
  domain                    = "vpc"
  network_interface         = aws_network_interface.web-server-nic.id
  associate_with_private_ip = "10.0.1.50"
  depends_on = [aws_instance.web-server-instance, aws_internet_gateway.gw]
}

#9. Create an ubuntu server and install/enable apache2
# todo: must be updated to users ami/key pair
# todo: update getting my own AMI code (??)
# terraform associated with setting server up in aws
resource "aws_instance" "web-server-instance" {
  ami           = "ami-053b0d53c279acc90"
  instance_type = "t2.micro"
  # todo: iam role with ec2 full access permissions
  iam_instance_profile = "ec2AdminDemo"
  availability_zone = "us-east-1a"
  # todo: update to appropriate instance key
  # your aws key pair
  # todo: creating my own key, using ssh - downloads a file with a .pem extension
  key_name =  "ryan-demo"
  # first device on the network
  network_interface {
    device_index         = 0
    network_interface_id = aws_network_interface.web-server-nic.id
  }
}

 output "server_public_ip" {
   value = aws_eip.one.public_ip
 }