package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import coil3.util.Logger
import com.plcoding.bookpedia.app.Route
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.presentation.book_detail.models.BookDetailAction
import com.plcoding.bookpedia.book.presentation.book_detail.models.BookDetailState
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel (
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.onStart {
        fetchBookDescription()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )
    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id
    fun onAction(action : BookDetailAction){
        when(action){
            BookDetailAction.OnBackClick -> {}
            BookDetailAction.OnFavClick -> {
                val book = _state.value.book?:return
                val isFav = _state.value.isFav
                viewModelScope.launch {
                    val isSuccess = if(isFav){
                        bookRepository.deleteFromFav(bookId)
                        true
                    }
                    else{
                        val result = bookRepository.markAsFav(book)
                        when(result){
                            is Result.Error<*> -> false
                            is Result.Success<*> -> true
                        }
                    }
                    if(isSuccess){
                        _state.update {
                            it.copy(isFav = !isFav)
                        }
                    }
                    else{
                        println("Error in book")
                    }
                }
            }
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update {
                    it.copy(book = action.book)
                }
            }
        }
    }

    fun fetchBookDescription(){
        viewModelScope.launch {
           bookRepository.getBookDescription(bookId)
               .onSuccess {
                   _state.update {bookSt->
                       bookSt.copy(book = bookSt.book?.copy(description = it), isLoading = false)
                   }
               }
               .onError {

               }
        }
    }
}