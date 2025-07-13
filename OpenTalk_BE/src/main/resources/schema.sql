CREATE TABLE "user" (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        full_name VARCHAR(255),
                        password VARCHAR(255),
                        is_enabled BOOLEAN,
                        last_hosted TIMESTAMP,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP,
                        company_branch_id BIGINT,
                        role_id BIGINT
);

CREATE TABLE "company_branch" (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  name VARCHAR(255),
                                  created_at TIMESTAMP,
                                  updated_at TIMESTAMP
);

CREATE TABLE "role" (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255),
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
);
