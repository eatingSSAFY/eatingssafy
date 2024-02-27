## IntelliJ setting

1. Local Environment variables setting
   1) Set (It's written in [notion](https://www.notion.so/ondol1224/373f5875fd0f4d9890a439b55cfc61d2?pvs=4))
      ```dtd
         BASIC_URL=${BASIC_URL};
         DB_NAME=${DB_NAME};
         DB_PASSWORD=${DB_PASSWORD};
         DB_USER_NAME=${DB_USER_NAME};
         JPA_DDL_AUTO=${JPA_DDL_AUTO};
         KAKAO_API_KEY=${KAKAO_API_KEY};
         KAKAO_CLIENT_SECRET=${KAKAO_CLIENT_SECRET};
         KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI};
         MAIL_PASSWORD=${MAIL_PASSWORD};
         MAIL_USERNAME=${MAIL_USERNAME};
         OCR_REQUEST_URL=${OCR_REQUEST_URL};
         OCR_SECRET_KEY=${OCR_SECRET_KEY};
         RESOURCE_DIR=${RESOURCE_DIR};
         SHOW_SQL=${SHOW_SQL};
      ```
2. If you're new to DB, you need to insert the initial value of allergy and lunch_time information.
   1) Import the `data.sql` under the `resources` directory into `eatingssafy` database.

   2) Import the `2024_ss_lunch_time.sql` under the `resources` directory into `eatingssafy` database.
    
3. Set 'out directory'
    1) Go to ‘Preferences > Build, Execution, Deployment > Build tools > Gradle’

    2) Set ‘Build and run using’ to ‘IntelliJ IDEA’

    3) Set ‘Run tests using’ to ‘IntelliJ IDEA’