class Conversation < ApplicationRecord
  belongs_to :post,optional: true
  has_many :participations, dependent: :destroy
  has_many :users, through: :participations
  has_many :messages, dependent: :destroy
end