#!/bin/bash

#STARTING COMMAND: bash fillDatabases.sh!

# Define project ENV variables
PROJECT_HOST="localhost"
PROJECT_PORT="8080"
PROJECT_API_URL="api"

# Define MySQL database credentials
DB_USER="root"
DB_PASSWORD="root"
DB_NAME="spring_reddit_clone"

# Define user data to register
USER_DATA=(
  '{"username":"milica1234","password":"milica1234","email":"milica123@gmail.com","avatar":"","description":"I am beautiful women. I love good mens.","displayName":"Milica Simic"}'
  '{"username":"ana5678","password":"ana5678","email":"ana5678@gmail.com","avatar":"","description":"I am a software engineer. I love coding.","displayName":"Ana Petrovic"}'
  '{"username":"marko1111","password":"marko1111","email":"marko1111@gmail.com","avatar":"","description":"I am a student. I love learning new things.","displayName":"Marko Markovic"}'
  '{"username":"jelena2222","password":"jelena2222","email":"jelena2222@gmail.com","avatar":"","description":"I am a writer. I love reading books and writing stories.","displayName":"Jelena Jankovic"}'
  '{"username":"stefan3333","password":"stefan3333","email":"stefan3333@gmail.com","avatar":"","description":"I am a musician. I love playing guitar and singing.","displayName":"Stefan Stankovic"}'
)

# Define community data
COMMUNITY_DATA=(
  '{"name":"Здраво Живот","description":"Заједница која промовише здрав начин живота, здраву исхрану и физичку активност.","flairs":["zdravlje","ishrana","fitnes"]}'
  '{"name":"Kreativna Oaza","description":"Mesto za kreativce svih vrsta, од писаца до сликара и музичара, где можете делити своје радове и идеје."}'
  '{"name":"Психолошка подршка","description":"Заједница која пружа подршку и савете особама које се суочавају са стресом, анксиозношћу, депресијом и другим психолошким изазовима.","flairs":["psihologija","saveti","mentalno_zdravlje"]}'
  '{"name":"Еколошка иницијатива","description":"Заједница која промовише одржив начин живота, заштиту животне средине и смањење отпада.","flairs":["ekologija","održivost","reciklaža"]}'
  '{"name":"Мој град","description":"Место за дискусију о актуелним темама и проблемима у вашем граду, предлоге за побољшање и иницијативе за заједницу.","flairs":["grad","aktuelnosti","inicijative"]}'
)

POST_DATA=(
    '{
        "communityName": "Здраво Живот",
        "text": "Убрзајте свој метаболизам уз ових 10 трикова!",
        "title": "Како да убрзам свој метаболизам?",
        "flairs": ["zdravlje","ishrana"]
    }'
    '{
        "communityName": "Здраво Живот",
        "text": "5 најбољих вежби за јачање грбног мускулатора",
        "title": "Како да јачам свој грбни мускулатор?",
        "flairs": []
    }'
    '{
        "communityName": "Здраво Живот",
        "text": "5 најздравијих замина за доручак",
        "title": "Који је најздравији доручак? Stručnjaci kažu sledeće ...",
        "flairs": ["ishrana"]
    }'
    '{
        "communityName": "Kreativna Oaza",
        "text": "Nova pesma koju sam napisao, nadam se da vam se sviđa!",
        "title": "Moja nova pesma - Odlazak",
        "flairs": []
    }'
    '{
        "communityName": "Kreativna Oaza",
        "text": "Moja nova slika ulje na platnu, inspirisana prirodom.",
        "title": "Pejzaž u ulju - Šuma",
        "flairs": []
    }'
    '{
        "communityName": "Kreativna Oaza",
        "text": "Prvo poglavlje iz mog novog romana, nadam se da će vam se svideti.",
        "title": "Moja nova knjiga - Skriveni svetovi",
        "flairs": []
    }'
    '{
        "communityName": "Психолошка подршка",
        "text": "Како се изборити са стресом на радном месту",
        "title": "Савети за борбу са стресом на раду",
        "flairs": ["saveti","mentalno_zdravlje"]
    }'
    '{
        "communityName": "Психолошка подршка",
        "text": "Како победити депресивне мисли",
        "title": "Савети за превазилажење депресивних мисли",
        "flairs": ["psihologija"]
    }'
    '{
        "communityName": "Психолошка подршка",
        "text": "Како се суочити са анксиозношћу у вези",
        "title": "Савети за борбу са анксиозношћу у вези",
        "flairs": ["psihologija","saveti"]
    }'
    '{
        "communityName": "Еколошка иницијатива",
        "text": "Како да направите компостер у свом дому? Ovo če Vam drastično olakšati život",
        "title": "Направите свој компостер!",
        "flairs": ["ekologija","održivost","reciklaža"]
    }'
    '{
        "communityName": "Еколошка иницијатива",
        "text": "Које су најбоље биоразградиве четкице за зубе i zašto?",
        "title": "Зашто би требало да користимо биоразградиве четкице за зубе?",
        "flairs": []
    }'
    '{
        "communityName": "Еколошка иницијатива",
        "text": "Како да смањите употребу пластике у свом дому?",
        "title": "Смањите употребу пластике!",
        "flairs": ["ekologija","održivost","reciklaža"]
    }'
    '{
        "communityName": "Мој град",
        "text": "Предлажемо усвојење новог просторног плана града који ће обезбедити више зелених површина и унапредити квалитет живота у граду.",
        "title": "Нови просторни план града",
        "flairs": ["grad","inicijative"]
    }'
    '{
        "communityName": "Мој град",
        "text": "Stručnjaci instituta za BBC na srpskom objašnjavaju šta građani mogu da urade da se zaštite od aerozagađenja.Како се борити против загађења ваздуха у нашем граду?",
        "title": "Загађење ваздуха у граду",
        "flairs": ["grad","aktuelnosti"]
    }'
    '{
        "communityName": "Мој град",
        "text": "Како да наш град постане пример одрживог развоја? Urbana sredina je područje sa većom gustinom naseljenosti i izraženim uticajem prisustva ljudi na uslove koji vladaju u području",
        "title": "Одрживи развој града",
        "flairs": ["grad","inicijative"]
    }'
)

COMMENT_DATA=(
    '{
    "text": "My first comment",
    "postId": 1
    }'
    '{
    "text": "Great post! I have been looking for ways to boost my metabolism.",
    "postId": 1
    }'
    '{
    "text": "I have a question - are there any foods or supplements that can help speed up metabolism?",
    "postId": 1
    }'
    '{
    "text": "I have been to 5 out of the 10 places on this list and I can say they are absolutely breathtaking. I am definitely adding the other 5 to my bucket list!",
    "postId": 2
    }'
    '{
    "text": "I would also recommend visiting Barcelona. The architecture and food there is amazing!",
    "postId": 3
    }'
    '{
      "text": "I have seen all of these movies except for Frozen 2. I guess I an m not really missing out on much.",
      "postId": 3
    }'
   '{
    "text": "Thanks for these tips! I have been struggling to train my new puppy and I am definitely going to try some of these techniques.",
    "postId": 4
    }'
    '{
      "text": "I think it is important to emphasize consistency when training a dog. If you are not consistent with your commands and rewards, your dog will get confused.",
      "postId": 4
    }'
    '{
      "text": "I would also recommend visiting Barcelona. The architecture and food there is amazing!",
      "postId": 4
    }'
    '{
      "text": "I used to be intimidated by weightlifting because I did not know how to use the equipment, but once I started I realized how empowering it is to lift heavy weights. I have never felt stronger!",
      "postId": 4
    }'
    '{
      "text": "I would also recommend making sure you have proper form when weightlifting. It is important to avoid injury and get the most out of your workouts.",
      "postId": 4
    }'
    '{
      "text": "Thanks for sharing this post, it was really informative! I never knew about these tips before.",
      "postId": 5
    }'
    '{
      "text": "I love how you included a variety of options for different dietary preferences. This post is really helpful!",
      "postId": 5
    }'
    '{
      "text": "I appreciate the research and effort you put into this post. It is great to have a reliable source of information on this topic.",
      "postId": 6
    }'
    '{
      "text": "As someone who struggles with meal planning, this post is a lifesaver! I can not wait to try out some of these recipes.",
      "postId": 6
    }'
    '{
      "text": "I like how you explained the benefits of each ingredient. It is not just about taste, it is about health too!",
      "postId": 6
    }'
    '{
      "text": "This post is perfect timing for me. I was just looking for new breakfast ideas and now I have 5 great options to choose from!",
      "postId": 8
    }'
    '{
      "text": "I have a food intolerance, so it is always difficult for me to find suitable recipes. These breakfast ideas are a game-changer, thank you!",
      "postId": 9
    }'
    '{
      "text": "I never thought of incorporating some of these ingredients into my breakfast routine, but I am excited to try them out now. Thanks for the inspiration!",
      "postId": 9
    }'
    '{
      "text": "I love how simple and easy these recipes are. I am not much of a cook, but I feel confident that I can make these breakfasts.",
      "postId": 9
    }'
    '{
      "text": "This post is a great reminder that breakfast really is the most important meal of the day. Thanks for the motivation to start my mornings off right!",
      "postId": 9
    }'
    '{
      "text": "I always struggle with eating a healthy breakfast because I am m always in a rush. These ideas are perfect for busy mornings, thank you!",
      "postId": 10
    }'
    '{
      "text": "I have been trying to eat more plant-based meals, so I appreciate the vegan and vegetarian options in this post. Thank you!",
      "postId": 10
    }'
    '{
      "text": "It is so easy to get stuck in a breakfast rut, but these ideas are a breath of fresh air. I can not wait to switch up my routine!",
      "postId": 10
    }'
    '{
      "text": "I never realized how much variety there could be in a healthy breakfast. These recipes are all so unique and interesting!",
      "postId": 11
    }'
    '{
      "text": "I am m excited to try out these recipes with my family. It is always great to find new meals that everyone can enjoy together.",
      "postId": 11
    }'
    '{
      "text": "Very interesting read, I learned a lot from this.",
      "postId": 12
    }'
    '{
      "text": "This post provides a lot of valuable information, thank you.",
      "postId": 13
    }'
    '{
      "text": "I love learning about ways to improve my health, so this post was perfect for me.",
      "postId": 13
    }'
    '{
      "text": "This is a well-written post with some great tips, thank you for sharing.",
      "postId": 13
    }'
    '{
      "text": "I had never considered some of these ideas before, so this post was very eye-opening.",
      "postId": 13
    }'
    '{
      "text": "I have been looking for information on this topic, so this post is very timely for me.",
      "postId": 13
    }'
    '{
      "text": "This is great advice, I will definitely be trying some of these tips.",
      "postId": 14
    }'
    '{
      "text": "I appreciate the insights in this post.",
      "postId": 14
    }'
    '{
      "text": "Very interesting read, I learned a lot from this.",
      "postId": 14
    }'
    '{
      "text": "Thanks for sharing this informative post!",
      "postId": 15
    }'
    '{
      "text": "I found this post very helpful, thanks for sharing.",
      "postId": 15
    }'
)

# Loop through user data and register each user
for user in "${USER_DATA[@]}"
do
  response=$(curl -s -X POST "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/register" -H 'Content-Type: application/json' -d "${user}")
  if [[ "$response" == *"User registration successful"* ]]; then
    echo "User registration successful"
  else
    echo "User registration failed: $response"
  fi
done

# Make first user as administrator

mysql -u ${DB_USER} -p${DB_PASSWORD} -e "USE ${DB_NAME};UPDATE user SET is_administrator = true WHERE user_id = 1;"

# Login as user[1] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[1]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# Adding some flairs

# Loop through community data array
for community_data in "${COMMUNITY_DATA[@]}"
do
  # Extract flairs from community data[i]
  flairs=$(echo "$community_data" | jq -r '.flairs[]')

  # Loop through flairs and send POST request to create them
  for flair in $flairs
  do
    curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer $AUTH_TOKEN" \
    -d "{\"name\": \"$flair\"}" http://localhost:8080/api/flair
  done
done

# user[1] creates community[1] without flairs
curl -s -H "Content-Type: application/json" -H "Authorization: Bearer ${AUTH_TOKEN}" -X POST -d "${COMMUNITY_DATA[1]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/communities/createCommunity"

# Login as user[3] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[3]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[3] creates post[4] on community[1]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[4]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[2] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[2] creates post[3] on community[1]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[3]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[3] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[3] creates post[5] on community[1]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[5]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

#user[3] creates community[0]
curl -s -H "Content-Type: application/json" -H "Authorization: Bearer ${AUTH_TOKEN}" -X POST -d "${COMMUNITY_DATA[0]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/communities/createCommunity"

# user[3] creates post[0] on community[0]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[0]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[5] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[3] creates post[2] on community[0]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[1] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[1] creates post[1] on community[0]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[1]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

#user[1] creates community[2]
curl -s -H "Content-Type: application/json" -H "Authorization: Bearer ${AUTH_TOKEN}" -X POST -d "${COMMUNITY_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/communities/createCommunity"

# user[1] creates post[6] on community[2]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[6]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[2] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[2] creates post[7] on community[2]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[7]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[4] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[4]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[4] creates post[8] on community[2]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[8]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

#user[4] creates community[3]
curl -s -H "Content-Type: application/json" -H "Authorization: Bearer ${AUTH_TOKEN}" -X POST -d "${COMMUNITY_DATA[3]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/communities/createCommunity"

# user[4] creates post[9] on community[3]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[9]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# user[4] creates post[11] on community[3]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[11]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[3] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[3]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[3] creates post[10] on community[3]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[10]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

#user[3] creates community[4]
curl -s -H "Content-Type: application/json" -H "Authorization: Bearer ${AUTH_TOKEN}" -X POST -d "${COMMUNITY_DATA[4]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/communities/createCommunity"

# user[4] creates post[12] on community[4]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[12]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"
# user[4] creates post[13] on community[4]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[13]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[2] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[2] creates post[14] on community[4]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "${POST_DATA[14]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/posts/createPost"

# Login as user[1] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[1]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[1] downvote post[2]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 2}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[1] downvote post[6]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 6}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[1] upvote post[9]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 9}'  "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[1] upvote post[11]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 11}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[1] downvote post[13]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 13}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[1] downvote post[15]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 15}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# Login as user[0] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[0]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[0] upvote post[2]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 2}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[0] upvote post[7]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 7}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[0] downvote post[9]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 9}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[0] upvote post[11]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 11}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[0] upvote post[13]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 13}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[0] downvote post[15]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 15}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# Login as user[2] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[2]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[2] downvote post[4]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 4}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[2] upvote post[7]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 7}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[2] downvote post[10]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 10}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[2] downvote post[11]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 11}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[2] downvote post[13]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 7}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[2] upvote post[15]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 15}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# Login as user[3] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[3]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[3] upvote post[4]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 4}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[3] upvote post[4]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 4}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[3] downvote post[8]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 8}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[3] upvote post[10]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 10}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[3] upvote post[12]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 12}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[3] upvote post[14]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 14}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# Login as user[4] and store authToken
loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[4]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

# user[4] upvote post[6]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 6}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[4] upvote post[8]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "UPVOTE", "postId": 8}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[4] downvote post[10]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 10}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[4] downvote post[12]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 12}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# user[4] downvote post[14]
curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d '{"reactionType": "DOWNVOTE", "postId": 14}' "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/reactions"

# posting comments from random logged in users
for comment_data in "${COMMENT_DATA[@]}"
do
  # Login as random user and store authToken
  loginResponse=$(curl -s -X POST -H "Content-Type: application/json" -d "${USER_DATA[$((RANDOM % 5))]}" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/auth/login")
  AUTH_TOKEN=$(echo "${loginResponse}" | jq -r '.authToken')

  curl -X POST -H "Authorization: Bearer ${AUTH_TOKEN}" -H "Content-Type: application/json" -d "$comment_data" "${PROJECT_HOST}:${PROJECT_PORT}/${PROJECT_API_URL}/comments/postComment"
done

##### IMPROVE SCRIPT - FIX ERRORS