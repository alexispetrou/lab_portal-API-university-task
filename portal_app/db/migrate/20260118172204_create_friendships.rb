class CreateFriendships < ActiveRecord::Migration[8.1]
  def change
    create_table :friendships do |t|
      t.references :user, null: false, foreign_key: true
      # Εδώ λέμε στο Rails ότι το friend_id δείχνει στον πίνακα :users
      t.references :friend, null: false, foreign_key: { to_table: :users }

      t.timestamps
    end
  end
end