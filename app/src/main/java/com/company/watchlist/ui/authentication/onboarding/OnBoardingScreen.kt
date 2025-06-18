package com.company.watchlist.ui.authentication.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPageState()

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val isTablet = maxWidth > 600.dp

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 64.dp else 16.dp)
        ) {

            OnBoardingViewPager(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                item = OnBoardingData.getItems(),
                pagerState = pagerState,
                isTablet = isTablet
            )

            // Pager indicator and buttons
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
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
                Spacer(Modifier.height(16.dp))

                if (pagerState.currentPage == 2) {
                    Button(
                        onClick = toLogIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Log In")
                    }

                    OutlinedButton(
                        onClick = toSignIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Sign Up")
                    }
                } else {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage < pagerState.pageCount - 1) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun OnBoardingViewPager(
    modifier: Modifier = Modifier,
    item: List<OnBoardingData.OnBoardingItem>,
    pagerState: PagerState,
    isTablet: Boolean
) {

    Column(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            count = item.count()
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(if (isTablet) 1.5f else 1f),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = item[page].image),
                    contentDescription = item[page].title
                )
                Spacer(modifier = Modifier.height(24.dp))
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

                Spacer(modifier = Modifier.weight(1f))
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