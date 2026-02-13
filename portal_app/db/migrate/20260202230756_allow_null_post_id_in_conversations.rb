class AllowNullPostIdInConversations < ActiveRecord::Migration[8.1]
  def change
    # Αλλάζουμε την κολώνα post_id ώστε να επιτρέπει το null
    change_column_null :conversations, :post_id, true
  end
end