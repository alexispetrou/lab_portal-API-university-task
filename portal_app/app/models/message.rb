class Message < ApplicationRecord
  belongs_to :user
  belongs_to :conversation  # ΑΥΤΟ ΕΙΝΑΙ ΤΟ ΚΛΕΙΔΙ
  belongs_to :recipient, class_name: "User", optional: true
end