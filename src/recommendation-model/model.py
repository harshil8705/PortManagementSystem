from sqlalchemy import create_engine
import pandas as pd
from config import DB_CONFIG

def get_engine():
    return create_engine(
        f"mysql+pymysql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}"
    )

def fetch_user_interactions():
    engine = get_engine()
    query = """
        SELECT u.user_id, p.port_name
        FROM port_interactions pi
        JOIN users u ON u.user_id = pi.user_fk_id
        JOIN ports p ON p.port_id = pi.port_fk_id
    """
    return pd.read_sql(query, engine)
