from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def recommend_ports(df, user_id, top_n=5):
    user_port_map = df.groupby('user_id')['port_name'].apply(lambda ports: ' '.join(ports)).reset_index()

    if user_id not in user_port_map['user_id'].values:
        return []

    vectorizer = CountVectorizer()
    port_matrix = vectorizer.fit_transform(user_port_map['port_name'])

    similarity = cosine_similarity(port_matrix)

    user_idx = user_port_map[user_port_map['user_id'] == user_id].index[0]

    similar_users = list(enumerate(similarity[user_idx]))
    similar_users = sorted(similar_users, key=lambda x: x[1], reverse=True)[1:]

    user_ports = set(df[df['user_id'] == user_id]['port_name'])
    recommendations = set()

    for idx, _ in similar_users:
        other_user_id = user_port_map.loc[idx, 'user_id']
        ports = set(df[df['user_id'] == other_user_id]['port_name'])
        recommendations.update(ports - user_ports)
        if len(recommendations) >= top_n:
            break

    return list(recommendations)[:top_n]
