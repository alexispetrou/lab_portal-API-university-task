class Post < ApplicationRecord
  belongs_to :user
  
  # ΑΥΤΟ ΛΕΙΠΕΙ:
  has_one :conversation, dependent: :destroy
  
  # Προαιρετικά: Αυτόματη δημιουργία chat με το που φτιάχνεται το post
  after_create :create_associated_conversation

  private

  def create_associated_conversation
    create_conversation(title: "Chat: #{self.title}")
  end
end