using System;
namespace Model.RestaurantOnline
{
    public class CategoryModel
    {
        private int id;
        private string eName;
        private string otherName;

        public int Id{
            get
            {
                return id;
            }

            set
            {
                this.id = value;
            }
        }

        public string EName{
            get
            {
                return eName;
            }

            set
            {
                this.eName = value;
            }
        }

        public string OtherName{
            get
            {
                return otherName;
            }

            set
            {
                this.otherName = value;
            }
        }
    }
}
