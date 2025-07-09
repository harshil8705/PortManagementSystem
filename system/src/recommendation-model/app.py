from flask import Flask, jsonify
from flask_cors import CORS
from model import fetch_user_interactions
from recommender import recommend_ports

app = Flask(__name__)
CORS(app)

@app.route("/recommend/<int:user_id>")
def recommend(user_id):
    try:
        df = fetch_user_interactions()
        ports = recommend_ports(df, user_id)
        return jsonify({
            "user_id": user_id,
            "recommended_ports": ports
        })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True, port=5000)
