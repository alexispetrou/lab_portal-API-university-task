class AddRecipientToMessages < ActiveRecord::Migration[8.1]
  def change
    add_column :messages, :recipient_id, :integer
    add_column :messages, :is_group, :boolean
  end
end
