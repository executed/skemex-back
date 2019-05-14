# Skemex Web Application

## Overview

Skemex is web application built for use in companies which need to have workplaces management 
system:

1. Serve as closed resource of the company which work with confidential information about its
 members and their relevant location.
    
2. Helps to easily manage company members workplaces, offers the ability to reserve workplace 
of another office for a certain time, provides engine of member location search etc.
 
3. Helps to understand the front-backend connectivity and can be used as fast start in some kind 
of web applications.

## How to use

#### Requirements:

1. Java: version 1.8+
2. Apache Tomcat :  version 8.0+
3. PostgreSQL RDMS: in case of repository cloning
4. Maven: 3.0+

#### Instructions:

1. Clone repository

2. Using Maven, create an archive with `.war` format: 

    in directory `~/skemex/` run following 
command:
 
        mvn clean install -DskipTests

3. Create database schema using following command: 

        CREATE DATABASE skemex
            WITH OWNER = postgres
            ENCODING = 'UTF8'
            TABLESPACE = pg_default
            LC_COLLATE = 'Ukrainian_Ukraine.1251'
            LC_CTYPE = 'Ukrainian_Ukraine.1251'
            CONNECTION LIMIT = -1;

5. Fill up database with initial data: 
   
   To use the application you need to have at least one user saved in system.
   For this run following PSQL script with all `${...}` replacements:   
   
        INSERT INTO public.office (id, time_zone, city, name) VALUES (1, '${TIME_ZONE}', '${CITY}', '${OFFICE_NAME}');    
        ALTER SEQUENCE office_id_seq RESTART WITH 2;
        INSERT INTO public.organization (id, name, owner_nickname, parent) VALUES (1, '${PROJECT}', null, null);
        ALTER SEQUENCE organization_id_seq RESTART WITH 2;
        INSERT INTO public."user" (nickname, password) VALUES ('${NICKNAME}', '${PASSWORD}');
        INSERT INTO public.users_roles (nickname, role_id) VALUES ('${NICKNAME}', 1);
        INSERT INTO public.employee (nickname, email, first_name, last_name,organization_id) 
        VALUES ('${NICKNAME}', null, '${F_NAME}', '${L_NAME}', 1);     
       
6. Deploy project on your Tomcat server: 
    
    Navigate to your Tomcat folder and paste recently created .war archive in folder `~/webapps/`
 
    Rename this archive to name "`api`".
    
    Put built frontend project from https://gitlab.com/ch-075/skemex-ui.git also in folder `~/webapps/`
    
    Navigate to folder `~/bin/` of your Tomcat and run following executable file:
    
    If you are using Windows: `startup.bat`, Linux: `startup.sh`
    
8. Access site by opening `http://localhost:8080/` in your browser  
    Use `NICKNAME` and `PASSWORD` from task №6 as your login credentials

9. After excel file upload (one of the navigation bar menus) you can resolve all merge conflicts 
using following steps:

    Open browser console with `Ctrl + Shift + C` and navigate to `Console`.
    
    Paste following commands:
    
        a = document.getElementsByClassName("my-0");
        for (var i = 0; i <a.length;i++){ if (a[i].innerText == "Excel"){ a[i].click(); }}
    Click `Enter` and wait some time till table with conflicts would be fully erased
    
### Good luck in your endeavors!!! 
