override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    // અહીં R.layout.custom_list_item વાપરવું
    val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_list_item, parent, false)
    return ViewHolder(view)
}

override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = items[position]
    holder.txtName.text = item.name
    // અહીંથી પણ કલર ફોર્સ કરવો
    holder.txtName.setTextColor(android.graphics.Color.BLACK)
    holder.itemView.setOnClickListener { onClick(item) }
}
