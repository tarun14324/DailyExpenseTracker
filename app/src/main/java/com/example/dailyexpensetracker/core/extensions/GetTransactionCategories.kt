package com.example.dailyexpensetracker.core.extensions

fun Boolean.getTransactionCategories(): List<String> {
    return if (this) {
        listOf(
            "Salary",
            "Freelance",
            "Investments",
            "Bonus",
            "Rental Income",
            "Other Income"
        )
    } else {
        listOf(
            "Groceries (Kirana)",
            "Mobile Recharge / Internet",
            "Rent",
            "Electricity Bill",
            "Water Bill",
            "LPG / Gas Refill",
            "Fuel (Petrol/Diesel/CNG)",
            "Metro/Train/Bus Fare",
            "Education Fees",
            "Healthcare / Medicines",
            "Insurance Premiums",
            "Swiggy / Zomato",
            "Shopping (Flipkart, Amazon)",
            "Subscriptions (Hotstar, JioCinema, etc.)",
            "Entertainment (Movies, Events)",
            "Travel (Ola/Uber, Flights, Hotels)",
            "Gifts / Donations",
            "EMI / Loan Repayment",
            "Credit Card Bill",
            "Personal Care",
            "Temple Offerings / Pooja Items",
            "Other Expenses"
        )
    }
}