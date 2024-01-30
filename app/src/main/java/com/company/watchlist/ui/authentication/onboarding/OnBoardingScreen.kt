package com.company.watchlist.ui.authentication.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.watchlist.data.OnBoardingData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    toLogIn: () -> Unit,
    toSignIn: () -> Unit,

) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPageState()
    var nextButtonPosition by remember { mutableFloatStateOf(0F) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(8.dp, 12.dp, 8.dp, 12.dp),
        ) {

            LaunchedEffect(key1 = pagerState) {
                scrollState.animateScrollTo(nextButtonPosition.roundToInt())
            }
            OnBoardingViewPager(
                item = OnBoardingData.getItems(),
                pagerState = pagerState
            )
            HorizontalPagerIndicator(
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 20.dp)
                    .align(Alignment.CenterHorizontally),
                pagerState = pagerState,
                inactiveColor = MaterialTheme.colorScheme.onPrimaryContainer,
                activeColor = MaterialTheme.colorScheme.primaryContainer,
                indicatorHeight = 8.dp,
                indicatorWidth = 12.dp,
                spacing = 12.dp
            )

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp, 40.dp, 40.dp, 0.dp),
                visible = pagerState.currentPage == 2
            ) {
                Button(
                    onClick = toLogIn
                ) {
                    Text(text = "Log In")
                }

            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp, 15.dp, 40.dp, 0.dp),
                visible = pagerState.currentPage == 2,
            ) {
                OutlinedButton(
                    onClick = toSignIn
                ) {
                    Text(text = "Sign Up")
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp, 60.dp, 40.dp, 0.dp)
                    .onGloballyPositioned { coordinates ->
                        nextButtonPosition = coordinates.positionInParent().y
                    },
                visible = pagerState.currentPage != 2
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1
                            )
                        }
                    }
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun OnBoardingViewPager(
    item: List<OnBoardingData.OnBoardingItem>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .padding(0.dp, 10.dp, 0.dp, 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                count = item.count()
            ) { page ->
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f),
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = item[page].image),
                        contentDescription = item[page].title
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = item[page].title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp)
                            .padding(top = 10.dp),
                        text = item[page].description,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun rememberPageState(
    @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
): PagerState = rememberSaveable(saver = PagerState.Saver) {
    PagerState(
        currentPage = initialPage,
    )
}