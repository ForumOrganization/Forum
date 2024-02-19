INSERT INTO users (first_name, last_name, email, username, password, role, status)
VALUES ('Yoana', 'Maksimova', 'yoana.maksimova@gmmail.com', 'yoana', 'Parola_1', 'ADMIN', 'ACTIVE'),
       ('Siyana', 'Baileva', 'siyana.baileva@gmail.com', 'siyana', 'Parola_2', 'USER', 'ACTIVE'),
       ('Veronika', 'Kashaykova', 'veronika.kashaykova@gmail.com', 'veronika', 'Parola_3', 'USER', 'ACTIVE'),
       ('Ivana', 'Petrova', 'ivana.m@gmail.com', 'ivana', 'Parola_4', 'USER', 'ACTIVE'),
       ('Trifon', 'Ivanov', 'triffon@gmail.com', 'trifon', 'Parola_5', 'USER', 'ACTIVE'),
       ('Evgeni', 'Dimitrov', 'dimitroff@gmail.com', 'evgeni', 'Parola_6', 'USER', 'ACTIVE'),
       ('Mila', 'Todorova', 'mila_123@gmail.com', 'mila', 'Parola_7', 'USER', 'ACTIVE'),
       ('Galya', 'Georgieva', 'galeto_91@gmail.com', 'galeto', 'Parola_8', 'USER', 'ACTIVE'),
       ('Silivia', 'Svilenova', 'sisi_89@gmail.com', 'sisi', 'Parola_9', 'USER', 'ACTIVE'),
       ('Deyan', 'Zhelev', 'dido_@gmail.com', 'dido', 'Parola_10', 'USER', 'ACTIVE'),
       ('Kaloyan', 'Petrov', 'kaloyan_petrov@gmail.com', 'kaloyan', 'Parola_11', 'USER', 'ACTIVE'),
       ('Kristina', 'Kaleva', 'krissi@gmail.com', 'krissi', 'Parola_12', 'USER', 'ACTIVE');
INSERT INTO phone_numbers (phone_number, user_id)
VALUES ('1234567890', 1);

INSERT INTO posts (created_by, title, content, creation_time)
VALUES (1, 'The Power of Mindful Eating', 'Hello all. I came across the phrase “mindful eating”. What do you know about it and how it can contribute to overall health and well-being?', NOW()),
       (2, 'Stress Management Techniques for a Balanced Life', 'Hi folks. I have problem to maintain work life balance. Can you please share some stress management techniques for a alanced life? Thanks!', NOW()),
       (3, 'The importance of good sleep.', 'Hello people! I was very stressed recently and I could not get enough sleep. I have spoken with my friend and he told me that sleep is vital for maintaining good health. Can you please share more details regarding the importance of sleep. I will be very grateful!', NOW()),
       (4, 'Which are the healthies food.', 'Hello all. I am planning to go for a big shopping during the weekend. I want to buy only healthy food. Can you please tell me which are the most healthy products that I can include into my meals to get the best health benefits for my body? Thanks in advance!', NOW()),
       (10, 'The health benefits of probiotics.', 'Hello dear friends. Today I made a research on the above topic. I want to share the wonderful insight I have read about this. Please feel free to comment and share your experience.
Probiotics are live microorganisms, primarily bacteria and some yeasts, that provide numerous health benefits when consumed in adequate amounts. Here are some of the key health benefits associated with probiotics:
Improved Digestive Health: Probiotics play a crucial role in maintaining a healthy balance of gut bacteria, which is essential for proper digestion and nutrient absorption. They help break down food, promote regular bowel movements, and prevent gastrointestinal issues such as constipation, diarrhea, and irritable bowel syndrome (IBS).
Support for Gut Microbiome: Probiotics contribute to the diversity and stability of the gut microbiome, which refers to the community of microorganisms residing in the digestive tract. A balanced gut microbiome is associated with better digestion, immune function, and overall health.
Boosted Immune Function: Probiotics help support a healthy immune system by enhancing the production of antibodies and stimulating immune cells in the gut. They also help prevent harmful bacteria from colonizing the intestines, reducing the risk of infections and promoting resistance to pathogens.
Reduction of Inflammation: Certain strains of probiotics have anti-inflammatory properties and can help reduce inflammation in the gut and throughout the body. Chronic inflammation is linked to various health conditions, including inflammatory bowel diseases (IBD), autoimmune disorders, and metabolic syndrome.
Management of Antibiotic-Related Side Effects: Probiotics can help mitigate the side effects of antibiotic use, such as diarrhea and gastrointestinal upset. Antibiotics can disrupt the balance of beneficial bacteria in the gut, leading to digestive issues. Taking probiotics alongside antibiotics can help restore balance and minimize these side effects.
Support for Mental Health: Emerging research suggests a link between gut health and mental health, often referred to as the gut-brain axis. Probiotics may play a role in supporting mental well-being by influencing neurotransmitter production, reducing stress, and improving mood regulation.
Prevention of Vaginal and Urinary Tract Infections: Probiotics can help maintain a healthy balance of vaginal and urinary tract flora, reducing the risk of infections such as bacterial vaginosis (BV), yeast infections, and urinary tract infections (UTIs).

Potential for Allergy Prevention: Some studies suggest that probiotics may help reduce the risk of developing allergies, particularly in infants and young children. Early exposure to beneficial bacteria may help regulate the immune system and reduce the likelihood of allergic reactions.
Support for Oral Health: Probiotics may contribute to oral health by inhibiting the growth of harmful bacteria in the mouth, reducing the risk of dental caries (cavities), gum disease, and bad breath.
It''s important to note that the specific health benefits of probiotics can vary depending on the strains and doses consumed, as well as individual factors such as age, health status, and diet. Incorporating probiotic-rich foods like yogurt, kefir, sauerkraut, kimchi, and kombucha into your diet, or taking probiotic supplements under the guidance of a healthcare professional, can help support overall health and well-being.
', NOW()),
       (2, 'Food rich in Omega 3.', 'Hi friends! Today I read that Omega 3 Is so important for our health. I will be very happy if your share your knowledge on which are the products that contain Omega 3? I appreciate your time and efforts. ', NOW()),
       (7, 'Best exercises for back pain.', 'Hello all. Recently I am working to much and I spend most of the time sitting. I started to feel back pain more often. Can you please recommend me exercises for back pain relieve. Thank you a lot!', NOW()),
       (5, 'Best food products for weigth loss.', 'Hello dear friends. I want to lose some pounds. I have tried with sport and some pills. Still the results are not as I hoped for. I am asking for help. I know that food is very important for maintaining healthy weigth. Do you know which are the best food products for boosting metabolism and promoting weigth loss? I very much appreciate your input! Thank you!', NOW()),
       (6, 'Food supplements for boosting metabolism.', 'Hello people. I rely on advice on the following topic: I am looking for food supplements that boost metabolism. Can you please share your knowledge on the above? I highly appreceite your insight. Thank you!', NOW()),
       (8, 'Is coffee good for health?', 'Hi all. I am 16 years old. I have never drunk coffee. All my friends have already started drinking coffee. I am confused. I have heard different opinions on the topic. Can you please share me your knowledge on the topic? Are there any health benefits or some risks associated with drinking coffee?', NOW()),
       (3, 'Healthiest water for daily use.', 'Hello everyone. I am looking for information on which water the healthiest? I mean tap water or mineral water, hot or cold, boiled or unboiled. One friend told me that she freeze the water, unfreeze it and after that she drinks it. This sounds crazy, but she told be that the water gain some qualities that make it very healthy. Can you please share your knowledge on the topic? I highly appreciate your opinion on the matter. Thank you!', NOW()),
       (11, 'Aromatherapy for vitalization.', 'Hello friends. My days always start very bad. I cannot wake up without at least 2 cups of coffee. I was thinking about more healthy way for waking up. A friend of mine told me that there is aromatherapy with some oils that vitalize the body and soul and improve concentration. Have you heard about it? Can you please recommend me oils? ', NOW()),
       (12, 'Supplements for boosting immune system.', 'Good evening! Recently I caught a flu. My husband is also experiencing some symptoms. I want to boost our immune systems but I am not sure which supplements give the best result. Can you please share your experience? Is ginger, honey and lemon the best immune boosters? ', NOW()),
       (7, 'The best cardio exercise for weigth loss.', 'Good morning! I want to lose some pounds. C an you please recommend me cardio exercises for weigth loss? Thank you!', NOW());


INSERT INTO comments (user_id, post_id, content, creation_time)
VALUES (10, 1, 'Mindful eating is a practice that involves paying attention to the present moment while eating, without judgment. It''s about being fully aware of the sensory experiences associated with food, such as the taste, texture, smell, and even the sounds of eating. This approach emphasizes cultivating a non-judgmental attitude towards food and one''s eating habits, promoting a healthier relationship with food and overall well-being. Here are several ways mindful eating can contribute to better health:
Promotes Healthy Eating Habits: Mindful eating encourages individuals to listen to their body''s hunger and fullness cues, helping them develop a more intuitive approach to eating. By paying attention to physical hunger and satiety signals, people are less likely to overeat or consume food out of boredom, stress, or other emotional triggers.
Enhances Digestion: Eating mindfully involves chewing food slowly and thoroughly, which aids in better digestion and nutrient absorption. By taking the time to savor each bite and fully engage with the eating process, individuals may experience reduced digestive discomfort and improved gastrointestinal function.
Increases Awareness of Food Choices: Mindful eating involves being conscious of food choices and their impact on health and well-being. By tuning into the sensory experience of eating, individuals may become more discerning about the foods they consume, opting for nourishing, whole foods that provide sustained energy and vitality.
Reduces Emotional Eating: Mindful eating encourages individuals to explore the underlying emotions and triggers that may lead to unhealthy eating habits, such as stress eating or binge eating. By cultivating awareness of emotional cues and practicing self-compassion, people can develop healthier coping mechanisms and break free from destructive eating patterns.
Improves Satisfaction and Enjoyment: By fully immersing themselves in the eating experience, individuals can derive greater pleasure and satisfaction from their meals. Mindful eating allows people to appreciate the flavors, textures, and aromas of food, leading to a more enjoyable and fulfilling dining experience.
Supports Weight Management: Research suggests that mindful eating may be beneficial for weight management, as it promotes a balanced and sustainable approach to eating. By fostering a deeper connection between mind and body, individuals may naturally regulate their food intake and make healthier choices, leading to gradual weight loss or maintenance.
Overall, mindful eating is not just about what you eat, but how you eat. By bringing awareness and intentionality to the act of eating, individuals can transform their relationship with food, cultivate greater self-awareness, and ultimately, enhance their overall health and well-being.
', NOW()),
       (10, 2, 'Managing stress is essential for maintaining overall health and well-being. Here are some stress management techniques that can help individuals achieve a balanced life:
Deep Breathing Exercises: Deep breathing exercises, such as diaphragmatic breathing or belly breathing, can help activate the body''s relaxation response, reducing stress and promoting a sense of calm. Encourage slow, deep breaths through the nose, followed by gentle exhales through the mouth.
Mindfulness Meditation: Mindfulness meditation involves focusing attention on the present moment without judgment. Regular practice of mindfulness meditation can help individuals cultivate awareness of their thoughts and emotions, leading to greater resilience and reduced stress reactivity.
Physical Activity: Engaging in regular physical activity, such as walking, jogging, yoga, or dancing, can help reduce stress levels and improve mood. Exercise releases endorphins, chemicals in the brain that act as natural painkillers and mood elevators.
Progressive Muscle Relaxation: Progressive muscle relaxation involves systematically tensing and relaxing different muscle groups in the body. This technique helps release physical tension and promotes relaxation throughout the body.
Healthy Lifestyle Habits: Adopting healthy lifestyle habits, such as maintaining a balanced diet, getting enough sleep, and avoiding excessive alcohol and caffeine consumption, can support overall well-being and resilience to stress.
Time Management Strategies: Effective time management can help individuals prioritize tasks, set realistic goals, and reduce feelings of overwhelm. Encourage the use of techniques such as to-do lists, time blocking, and prioritization to manage workload and minimize stress.
Social Support: Building strong social connections and seeking support from friends, family, or support groups can provide emotional support during times of stress. Having a supportive network of people to lean on can help individuals navigate challenging situations more effectively.

Mindful Eating: Mindful eating involves paying attention to the sensory experience of eating, such as taste, texture, and smell, without judgment. By practicing mindful eating, individuals can cultivate a healthier relationship with food and reduce stress-related eating behaviors.
Relaxation Techniques: Encourage the use of relaxation techniques such as guided imagery, visualization, or listening to calming music to promote relaxation and reduce stress levels.
Seeking Professional Help: In cases of chronic or severe stress, it''s important to seek support from a mental health professional. Therapy, counseling, or other interventions can provide individuals with the tools and resources they need to manage stress effectively and improve overall well-being.
By incorporating these stress management techniques into their daily routine, individuals can cultivate resilience, reduce stress levels, and achieve greater balance in their lives.
', NOW()),
       (9, 3, 'Quality sleep is essential for overall health and well-being. It plays a crucial role in various aspects of physical, mental, and emotional health. Here are some reasons why quality sleep is important:
Restoration and Repair: During sleep, the body undergoes essential processes of repair, restoration, and regeneration. This includes repairing tissues, muscles, and cells, as well as replenishing energy stores. Quality sleep allows the body to recover from the wear and tear of daily activities, promoting optimal physical health.
Cognitive Function: Adequate sleep is vital for cognitive function, including memory, concentration, and decision-making abilities. During sleep, the brain consolidates memories, processes information, and clears out toxins that accumulate during waking hours. Quality sleep enhances cognitive performance and promotes mental clarity and alertness.
Emotional Regulation: Sleep plays a crucial role in emotional regulation and mood stability. Lack of sleep can disrupt neurotransmitter levels in the brain, leading to irritability, mood swings, and heightened emotional reactivity. Quality sleep supports emotional well-being and resilience to stress.
Immune Function: Sleep is closely linked to immune function, with insufficient sleep impairing the body''s ability to fight off infections and illnesses. During sleep, the immune system releases cytokines, proteins that help regulate inflammation and immune response. Quality sleep strengthens the immune system, reducing the risk of infections and promoting overall health.
Metabolic Health: Adequate sleep is important for maintaining a healthy metabolism and weight. Sleep deprivation can disrupt hormonal balance, leading to increased hunger and cravings for high-calorie foods. It also affects insulin sensitivity, glucose metabolism, and appetite regulation, increasing the risk of obesity and metabolic disorders.
Cardiovascular Health: Quality sleep is essential for cardiovascular health, with sleep deprivation being linked to an increased risk of heart disease, hypertension, and stroke. During sleep, the body regulates blood pressure, heart rate, and blood vessel function. Quality sleep supports cardiovascular function and reduces the risk of cardiovascular events.
Physical Performance: Optimal sleep is essential for athletic performance and physical recovery. Sleep deprivation can impair coordination, reaction time, and muscle recovery, leading to decreased athletic performance and increased risk of injury. Quality sleep enhances physical performance, endurance, and muscle recovery.
Overall Well-Being: Quality sleep is essential for overall health, vitality, and well-being. It helps regulate mood, energy levels, and stress resilience, enabling individuals to function optimally in their daily lives. Prioritizing quality sleep is an important aspect of self-care and promotes a balanced and fulfilling lifestyle.
In summary, quality sleep is essential for physical health, cognitive function, emotional well-being, and overall vitality. By prioritizing sleep and adopting healthy sleep habits, individuals can optimize their health and enhance their quality of life.
', NOW()),
       (10, 4, 'The most healthy foods are those that are nutrient-dense, meaning they provide a high amount of essential nutrients relative to their calorie content. Here are some examples of highly nutritious foods that are often considered to be among the healthiest:
Vegetables: Leafy greens like spinach, kale, and Swiss chard are packed with vitamins, minerals, and antioxidants. Other nutrient-rich vegetables include broccoli, Brussels sprouts, bell peppers, carrots, and sweet potatoes.

Fruits: Berries such as blueberries, strawberries, and raspberries are loaded with antioxidants and vitamins. Other nutritious fruits include oranges, apples, bananas, kiwi, and avocado.
Whole Grains: Whole grains like oats, quinoa, brown rice, barley, and bulgur are rich in fiber, vitamins, and minerals. They provide sustained energy and support digestive health.
Lean Proteins: Lean protein sources such as chicken breast, turkey, fish (salmon, tuna, trout), tofu, tempeh, and legumes (beans, lentils, chickpeas) are essential for muscle repair and growth. They also help keep you feeling full and satisfied.
Healthy Fats: Foods rich in healthy fats, such as nuts (almonds, walnuts, pistachios), seeds (chia seeds, flaxseeds), olive oil, avocado, and fatty fish (salmon, mackerel, sardines), provide omega-3 fatty acids and support heart health, brain function, and inflammation reduction.
Dairy or Dairy Alternatives: Dairy products like Greek yogurt and cottage cheese are excellent sources of protein and calcium. For those who are lactose intolerant or prefer non-dairy options, fortified plant-based alternatives like almond milk, soy milk, and oat milk are available.
Herbs and Spices: Herbs and spices like turmeric, cinnamon, ginger, garlic, and oregano not only add flavor to dishes but also contain powerful antioxidants and anti-inflammatory properties that contribute to overall health.
Legumes and Pulses: Legumes such as beans, lentils, chickpeas, and peas are rich in protein, fiber, vitamins, and minerals. They are also low in fat and can help lower cholesterol levels and regulate blood sugar.
Probiotic Foods: Foods like yogurt, kefir, sauerkraut, kimchi, and kombucha contain beneficial bacteria that support gut health and digestion.
Water: While not a food, water is essential for overall health. Staying hydrated is crucial for regulating body temperature, supporting digestion, transporting nutrients, and maintaining proper bodily functions.
Incorporating a variety of these nutrient-dense foods into your diet can help promote overall health, support a strong immune system, and reduce the risk of chronic diseases. It''s also important to focus on balance, moderation, and enjoying a wide range of foods to ensure you''re meeting all of your nutritional needs.
', NOW()),
       (8, 6, 'Omega-3 fatty acids are essential fats that play a crucial role in maintaining overall health, particularly heart health, brain function, and inflammation regulation. Here are some foods that are good sources of omega-3 fatty acids:
Fatty Fish: Fatty fish are among the richest sources of omega-3 fatty acids, particularly EPA (eicosapentaenoic acid) and DHA (docosahexaenoic acid). Examples include:
Salmon
Mackerel
Sardines
Trout
Herring
Anchovies
Albacore tuna
Flaxseeds: Flaxseeds and flaxseed oil are excellent plant-based sources of omega-3 fatty acids, particularly alpha-linolenic acid (ALA), a type of omega-3 that the body can convert into EPA and DHA to a limited extent.
Chia Seeds: Chia seeds are another plant-based source of ALA omega-3 fatty acids. They can be added to smoothies, yogurt, oatmeal, or baked goods to boost omega-3 intake.
Walnuts: Walnuts are a good source of ALA omega-3 fatty acids and provide a convenient plant-based option for incorporating omega-3s into the diet.
Hemp Seeds: Hemp seeds are rich in ALA omega-3 fatty acids and are also a good source of protein, fiber, and other essential nutrients.
Soybeans and Soy Products: Soybeans and soy-based products like tofu and tempeh contain ALA omega-3 fatty acids. Incorporating these foods into your diet can help increase omega-3 intake, especially for vegetarians and vegans.
Canola Oil: Canola oil is a good source of ALA omega-3 fatty acids and can be used in cooking and salad dressings to boost omega-3 content.
Seaweed and Algae: Certain types of seaweed and algae, such as spirulina and chlorella, contain EPA and DHA omega-3 fatty acids. These plant-based sources are particularly beneficial for individuals following a vegetarian or vegan diet.
Incorporating a variety of these omega-3-rich foods into your diet can help ensure an adequate intake of these essential fatty acids, which are important for supporting heart health, brain function, and overall well-being. If you have specific dietary needs or concerns, consider consulting with a healthcare professional or registered dietitian for personalized advice.

', NOW()),
       (10, 7, 'Exercising regularly is important for managing and preventing back pain. However, it''s essential to choose exercises that strengthen the muscles supporting the spine, improve flexibility, and promote proper posture without exacerbating existing pain. Here are some of the best exercises for relieving and preventing back pain:

Walking: Walking is a low-impact aerobic exercise that helps strengthen the muscles in the legs, buttocks, and core while improving overall cardiovascular health. It also helps maintain proper posture and spinal alignment.

Swimming: Swimming and water-based exercises are excellent options for individuals with back pain because they provide a full-body workout with minimal impact on the joints. Swimming helps strengthen the muscles of the back, shoulders, and core while improving flexibility and range of motion.

Yoga: Yoga combines stretching, strength-building, and relaxation techniques to alleviate back pain and improve overall spinal health. Certain yoga poses, such as cat-cow, child''s pose, downward-facing dog, and cobra pose, can help stretch and strengthen the muscles of the back, abdomen, and hips.

Pilates: Pilates focuses on strengthening the core muscles, including those in the abdomen, pelvis, and lower back, which are crucial for maintaining spinal stability and proper posture. Pilates exercises can help alleviate back pain and improve overall strength and flexibility.

Tai Chi: Tai Chi is a gentle form of martial arts that involves slow, flowing movements and deep breathing exercises. It promotes relaxation, balance, and flexibility while strengthening the muscles of the legs, hips, and core, which can help alleviate back pain and improve overall mobility.

Core Strengthening Exercises: Strengthening the muscles of the core, including the abdominals, obliques, and lower back, is essential for supporting the spine and reducing back pain. Exercises such as plank variations, bird-dog, bridge, and pelvic tilts can help strengthen the core muscles and improve spinal stability.

Stretching Exercises: Gentle stretching exercises can help alleviate tightness and tension in the muscles of the back, hips, and legs, which can contribute to back pain. Focus on stretches that target the hamstrings, hip flexors, quadriceps, and lower back, such as hamstring stretches, hip flexor stretches, and spinal twists.

Low-Impact Aerobic Exercises: In addition to walking and swimming, other low-impact aerobic exercises such as cycling, elliptical training, and using a recumbent exercise bike can help improve cardiovascular health and strengthen the muscles supporting the spine without putting excessive strain on the back.

It''s important to start slowly and gradually increase the intensity and duration of exercise to avoid exacerbating existing back pain. If you have severe or chronic back pain, consult with a healthcare professional or physical therapist before starting any new exercise program. They can provide personalized recommendations and guidance based on your specific needs and limitations.
', NOW()),
       (10, 9, 'When it comes to weight loss, incorporating certain ingredients into your diet can help support your goals by promoting satiety, boosting metabolism, and providing essential nutrients while keeping calorie intake in check. Here are some of the best ingredients for weight loss:

Vegetables: Non-starchy vegetables like leafy greens, broccoli, cauliflower, bell peppers, and cucumbers are low in calories but high in fiber, vitamins, and minerals. They help fill you up without adding excess calories, making them ideal for weight loss.

Lean Proteins: Lean protein sources such as chicken breast, turkey, fish, tofu, tempeh, legumes (beans, lentils, chickpeas), and low-fat dairy products are important for muscle maintenance and satiety. Protein-rich foods help keep you feeling full and satisfied, reducing hunger and preventing overeating.

Whole Grains: Whole grains like oats, quinoa, brown rice, barley, and bulgur are rich in fiber, which helps regulate digestion and promote feelings of fullness. They also provide sustained energy and help stabilize blood sugar levels, preventing spikes and crashes that can lead to cravings and overeating.

Healthy Fats: Incorporating healthy fats into your diet can help promote satiety and support overall health. Sources of healthy fats include avocados, nuts (almonds, walnuts, pistachios), seeds (chia seeds, flaxseeds), olive oil, and fatty fish (salmon, mackerel, sardines).

Fruits: Fruits like berries (strawberries, blueberries, raspberries), apples, oranges, and pears are rich in fiber, vitamins, and antioxidants, making them nutritious choices for weight loss. While fruits contain natural sugars, they are also high in water content and fiber, which helps keep you feeling full.

Legumes: Legumes such as beans, lentils, chickpeas, and peas are excellent sources of plant-based protein and fiber. They help promote satiety, regulate blood sugar levels, and support digestive health, making them beneficial for weight loss.

Spices and Herbs: Certain spices and herbs, such as cinnamon, ginger, turmeric, cayenne pepper, and black pepper, have been shown to boost metabolism, increase calorie burning, and aid in weight loss. Adding these ingredients to your meals can enhance flavor and promote weight loss.
Water: While not a specific ingredient, staying hydrated is essential for weight loss. Drinking an adequate amount of water throughout the day helps keep you hydrated, reduces hunger, and supports proper digestion and metabolism. Aim to drink plenty of water, and consider replacing sugary beverages with water or herbal tea.
', NOW()),
       (2, 10, 'While food supplements can play a role in supporting metabolism, it''s important to note that they are not a magic solution for weight loss or boosting metabolism. However, certain supplements may have ingredients that can help support metabolic function when combined with a healthy diet and lifestyle. Here are some of the best food supplements for supporting metabolism:
Caffeine: Caffeine is a natural stimulant found in coffee, tea, and some supplements. It can increase metabolic rate and enhance fat oxidation, leading to temporary increases in energy expenditure.

Green Tea Extract: Green tea extract contains catechins, compounds that have been shown to boost metabolism and promote fat loss. It may also have antioxidant properties and support overall health.
Capsaicin: Capsaicin is the compound that gives chili peppers their spicy flavor. It has been found to increase metabolic rate and promote fat burning by activating thermogenesis, the process by which the body produces heat.
L-Carnitine: L-Carnitine is an amino acid that plays a role in fat metabolism. It helps transport fatty acids into the mitochondria, where they can be burned for energy. Some studies suggest that L-carnitine supplements may help increase fat oxidation and improve exercise performance.
Conjugated Linoleic Acid (CLA): CLA is a type of fatty acid found in meat and dairy products, as well as in supplement form. Some research suggests that CLA supplements may help reduce body fat and increase lean muscle mass by boosting metabolism and promoting fat oxidation.
Iron: Iron is essential for the production of hemoglobin, which carries oxygen to cells and tissues throughout the body. Adequate iron levels are important for maintaining a healthy metabolism and energy levels. Iron supplements may be beneficial for individuals with iron deficiency anemia or low iron levels.
B Vitamins: B vitamins, including B1 (thiamine), B2 (riboflavin), B3 (niacin), B5 (pantothenic acid), B6 (pyridoxine), B7 (biotin), and B12 (cobalamin), play a crucial role in metabolism by helping convert food into energy. B vitamin supplements or B-complex supplements may be beneficial for individuals with deficiencies or those looking to support metabolic function.
Chromium: Chromium is a mineral that plays a role in carbohydrate and fat metabolism. Some studies suggest that chromium supplements may help improve insulin sensitivity, regulate blood sugar levels, and support weight management.
It''s important to consult with a healthcare professional before starting any new supplement regimen, especially if you have underlying health conditions or are taking medications. Additionally, supplements should be used in conjunction with a balanced diet, regular exercise, and other healthy lifestyle habits for optimal results.
', NOW()),
       (3, 11, 'Hi dear! Here are some of the advantages associated with drinking coffee:
Potential Health Benefits of Coffee:
Antioxidant Properties: Coffee is rich in antioxidants, such as chlorogenic acid and polyphenols, which have been linked to various health benefits, including reduced inflammation and protection against chronic diseases like heart disease and certain types of cancer.
Improved Mental Alertness: Caffeine, the primary active ingredient in coffee, is a central nervous system stimulant that can enhance mental alertness, concentration, and cognitive function. Moderate caffeine intake has been associated with improved mood, focus, and reaction time.
Reduced Risk of Certain Diseases: Some studies suggest that moderate coffee consumption may be associated with a lower risk of certain diseases, including Parkinson''s disease, Alzheimer''s disease, type 2 diabetes, and liver disease.
Enhanced Physical Performance: Caffeine has been shown to increase adrenaline levels and mobilize fatty acids from fat tissues, leading to improved physical performance and endurance during exercise. It may also help reduce perceived exertion and delay fatigue.
Support for Weight Management: Caffeine can increase metabolism and promote thermogenesis, the process by which the body generates heat and burns calories. Some research suggests that caffeine may help suppress appetite and promote fat oxidation, which could aid in weight management.
', NOW()),
       (5, 11, 'I have another opinion. I have received many on the topic. There are some detrimental for your health risks that you should consider. Please read the below info:
Potential Risks and Considerations:
Caffeine Sensitivity: Some individuals may be more sensitive to the effects of caffeine and may experience symptoms such as anxiety, jitteriness, insomnia, or heart palpitations with even small amounts of coffee. It''s important to listen to your body and limit caffeine intake if you experience adverse effects.
Sleep Disruption: Consuming coffee, especially in the afternoon or evening, can interfere with sleep quality and duration. Caffeine has a stimulatory effect on the central nervous system and can disrupt the body''s natural sleep-wake cycle, leading to insomnia or restless sleep.
Digestive Issues: Coffee can stimulate stomach acid production, which may exacerbate gastrointestinal issues such as acid reflux, heartburn, or gastritis in some individuals. Drinking coffee on an empty stomach or in large quantities may increase the risk of digestive discomfort.

Addiction and Withdrawal: Regular consumption of caffeine-containing beverages like coffee can lead to dependence and tolerance, meaning you may need to consume more coffee to achieve the same effects over time. Withdrawal symptoms, such as headaches, fatigue, and irritability, may occur when caffeine intake is abruptly reduced or discontinued.
Potential Impact on Blood Pressure and Heart Health: While moderate coffee consumption is generally considered safe for most people, excessive caffeine intake may raise blood pressure and heart rate, especially in individuals with hypertension or heart conditions. It''s important to monitor caffeine intake and limit consumption if you have underlying cardiovascular issues.
', NOW());


INSERT INTO reactions_posts (type_reaction, user_id, post_id)
VALUES ('LIKES', 1, 1),
       ('LIKES', 4, 4),
       ('LIKES', 7, 5),
       ('LIKES', 10, 1),
       ('LIKES', 1, 2),
       ('LIKES', 2, 7),
       ('LIKES', 1, 3),
       ('LIKES', 1, 4),
       ('LIKES', 3, 6),
       ('LIKES', 12, 1),
       ('LIKES', 10, 1),
       ('LIKES', 11, 1),
       ('LIKES', 12, 10),
       ('LIKES', 11, 1),
       ('LIKES', 10, 9),
       ('LIKES', 1, 5),
       ('LIKES', 1, 6),
       ('DISLIKES', 2, 9),
       ('LIKES', 9, 10),
       ('LIKES', 11, 9),
       ('LOVE', 3, 10),
       ('LIKES', 4, 1),
       ('LIKES', 6, 12),
       ('DISLIKES', 7, 9);

# INSERT INTO reactions_comments (type_reaction, user_id,comment_id)
# VALUES('LIKES', 6, 12);




INSERT INTO tags (name)
VALUES ('#omega 3'),
       ('#sport'),
       ('#coffee'),
       ('#weight loss'),
       ('#back pain'),
       ('#vitalization'),
       ('#supplements'),
       ('#swimming'),
       ('#healthy living'),
       ('#antioxidants'),
       ('#water'),
       ('#aromatherapy'),
       ('#lavender'),
       ('#immune system'),
       ('#cardio exercise'),
       ('#caffeine'),
       ('#boosting metabolism'),
       ('#probiotics'),
       ('#good sleep'),
       ('#mindful eating'),
       ('#Food');


INSERT INTO post_tags (post_id, tag_id)
VALUES (4, 1),
       (6, 1),
       (2, 2),
       (7, 2),
       (11, 3),
       (2, 4),
       (9, 4),
       (10, 4),
       (7, 5),
       (13, 6),
       (14, 7),
       (10, 7),
       (5, 7),
       (7, 7),
       (7, 8),
       (12, 11),
       (13, 12),
       (13, 13),
       (14, 14),
       (10, 16),
       (11, 16);



