/*usage <path>/cqlsh < src/core/cql/schema.sql*/

/*Key space*/
/*Use your keyspace name*/
CREATE KEYSPACE core WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE core;
/*Early Access*/
CREATE TABLE early_access (email text PRIMARY KEY);

/*users*/
CREATE TABLE user_account (user_id timeuuid PRIMARY KEY, username text, email text, password text, first_name text, last_name text, active boolean);
CREATE TABLE user_profile (user_id timeuuid PRIMARY KEY, first_name text, middle_name text, last_name text, email text, website text, phone_mobile text, phone_fixed text,
       address_line1 text, street text, city text, state text, country text, profile_photo text, 
       gender text, birth_date text, birth_year int, chat_channel text, chat_id text);
CREATE TABLE user_login_username(username text PRIMARY KEY, user_id timeuuid);
CREATE TABLE user_login_email(email text PRIMARY KEY, user_id timeuuid);
CREATE TABLE user_session (auth_token text PRIMARY KEY, session_expire_time timestamp, user_id timeuuid);
CREATE TABLE user_session_index (user_id timeuuid, auth_token text, PRIMARY KEY(user_id, auth_token));

CREATE TABLE user_account_registration(registration_key timeuuid PRIMARY KEY, user_id timeuuid);
CREATE TABLE password_reset(password_reset_key timeuuid PRIMARY KEY, user_id timeuuid, expiry timestamp);

/*CMS*/
CREATE TABLE site (site_id uuid PRIMARY KEY, site_name text, domain_name text, active boolean);
CREATE TABLE site_domain(domain_name text PRIMARY KEY, site_id uuid);
CREATE TABLE site_admin (user_id timeuuid, site_id uuid, PRIMARY KEY (user_id, site_id));
CREATE TABLE page (url text, site_id uuid, title text, content text, meta_tags text, meta_description text, PRIMARY KEY (url, site_id));

/*MOBILE API*/
CREATE TABLE api_app_key(app_key text PRIMARY KEY, platform text, version text);
CREATE TABLE api_request_token(request_token text PRIMARY KEY, app_key text, device_id text);

CREATE TABLE api_device_request_token(request_token text PRIMARY KEY, platform text, model text, version text);
CREATE TABLE api_device_auth_token(auth_token text PRIMARY KEY, platform text, model text, version text);

CREATE TABLE box(box_id timeuuid, user_id timeuuid, box_name text, created_date timestamp, active boolean, PRIMARY KEY(box_id, user_id));
CREATE TABLE box_handle(box_name text PRIMARY KEY, box_id_id timeuuid);
CREATE TABLE box_admin(box_id timeuuid PRIMARY KEY, user_id timeuuid);
CREATE TABLE box_connect(user_id timeuuid, box_id timeuuid, PRIMARY KEY(user_id, box_id));
CREATE TABLE box_connect_index(box_id timeuuid, user_id timeuuid, PRIMARY KEY (box_id, user_id));
CREATE TABLE box_garbage(box_id timeuuid PRIMARY KEY);

CREATE TABLE feed(box_id timeuuid, feed_id timeuuid, content text, location_name text, location_address text, location_latitude decimal, location_longitude decimal, event_name text, event_description text, event_time timestamp, PRIMARY KEY (box_id, feed_id));

CREATE TABLE feed_image (feed_id timeuuid PRIMARY KEY, image_url text);
/*CREATE TABLE feed_youtube_video(feed_id timeuuid PRIMARY KEY, youtube_url text);*/


/*Store*/

/*
CREATE TABLE product (product_id timeuuid PRIMARY KEY, title text, description text, price decimal, stock decimal);
CREATE TABLE category(category_id timeuuid PRIMARY KEY, category_name)
CREATE TABLE category_product(category_id timeuui PRIMARY KEY, product_id timeuuid);
CREATE TABLE category_tree(parent_id timeuuid PRIMARY KEY, category_id timeuuid)
CREATE TABLE product_review(product_id timeuuid, user_id timeuuid, comment text, rating int, review_date timestamp, PRIMARY KEY (product_id, user_id));
*/
