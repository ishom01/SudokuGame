package id.ishom.sudokuapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_sudoku.view.*

class SudokuAdapter(private val context: Context, private var boards: ArrayList<Board>): RecyclerView.Adapter<SudokuAdapter.ViewHolder>() {

    var selectedPosition: Int? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val itemLayout = view.itemLayout
        val itemFrameLayout = view.itemFrameLayout
        val valueTextView = view.valueTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sudoku, parent, false))
    }

    fun updateData(boards: ArrayList<Board>) {
        this.boards = boards
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board = boards[position]
        holder.valueTextView.text = board.value?.toString()
        if (board.isQuestion) {
            holder.valueTextView.setTextColor(context.resources.getColor(R.color.grey))
        } else {
            holder.valueTextView.setTextColor(context.resources.getColor(R.color.white))
        }

        if (board.isValid) {
            holder.itemLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
        } else {
            holder.itemLayout.setBackgroundColor(context.resources.getColor(R.color.red))
        }

        if (position == selectedPosition && !board.isQuestion) {
            holder.itemFrameLayout.setBackgroundResource(R.drawable.bg_selected_board)
        } else {
            holder.itemFrameLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
        }

        holder.itemLayout.setOnClickListener {
            if (!board.isQuestion) {
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return boards.size
    }
}